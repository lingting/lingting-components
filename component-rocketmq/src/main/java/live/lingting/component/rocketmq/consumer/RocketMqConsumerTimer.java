package live.lingting.component.rocketmq.consumer;

import live.lingting.component.rocketmq.RocketMqMessage;
import live.lingting.component.rocketmq.RocketMqMessageConsumer;
import live.lingting.component.core.thread.AbstractTimer;
import live.lingting.component.core.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-05-26 14:15
 */
@RequiredArgsConstructor
public class RocketMqConsumerTimer extends AbstractTimer {

	private final AbstractPullConsumer<?> pull;

	private final RocketMqMessageConsumer messageConsumer;

	@Override
	public String getSimpleName() {
		return "RocketMqConsumer-" + pull.getTopic();
	}

	@Override
	public long getTimeout() {
		return 0;
	}

	@Override
	public boolean isRun() {
		return super.isRun() && pull.getConsumer().isRunning();
	}

	@Override
	protected void process() throws Exception {
		DefaultLitePullConsumer consumer = pull.getConsumer();

		List<MessageExt> poll = consumer.poll();
		if (CollectionUtils.isEmpty(poll)) {
			long millis = pull.getPullInterval();
			Thread.sleep(millis);
			return;
		}
		List<RocketMqMessage> collect = poll.stream()
			.map(pull::of)
			.filter(message -> message.getTopic() != null)
			.collect(Collectors.toList());
		try {
			consumer(collect);
		}
		finally {
			// 处理完成, 如果不是自动提交则手动提交
			if (!consumer.isAutoCommit()) {
				consumer.commitSync();
			}
		}
	}

	void consumer(List<RocketMqMessage> messages) {
		if (!CollectionUtils.isEmpty(messages)) {
			messageConsumer.batch(messages);
		}
	}

}
