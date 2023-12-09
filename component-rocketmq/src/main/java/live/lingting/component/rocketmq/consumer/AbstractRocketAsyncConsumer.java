package live.lingting.component.rocketmq.consumer;

import live.lingting.component.core.util.IdUtils;
import live.lingting.component.core.util.ThreadUtils;
import live.lingting.component.rocketmq.message.RocketMqMessage;

import java.util.List;

/**
 * @author lingting 2023-10-25 14:13
 */
public abstract class AbstractRocketAsyncConsumer extends AbstractRocketMqConsumer {

	protected String traceId(RocketMqMessage message) {
		return message.getKeys();
	}

	protected String threadName(RocketMqMessage message) {
		return String.format("%s-%s", message.getTopic(), message.getKeys());
	}

	@Override
	protected void batch(List<RocketMqMessage> rocketMqMessages) {
		for (RocketMqMessage message : rocketMqMessages) {
			String traceId = traceId(message);
			String name = threadName(message);
			ThreadUtils.execute(name, () -> {
				IdUtils.fillTraceId(traceId);
				try {
					accept(message);
				}
				finally {
					IdUtils.remoteTraceId();
				}
			});
		}
	}

	protected abstract void accept(RocketMqMessage message);

}
