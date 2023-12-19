package live.lingting.component.rocketmq.consumer;

import live.lingting.component.core.context.ContextComponent;
import live.lingting.component.rocketmq.message.RocketMqMessage;
import lombok.Getter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author lingting 2023-06-08 14:59
 */
@SuppressWarnings({ "java:S6813", "java:S1452" })
public abstract class AbstractRocketMqConsumer implements ContextComponent {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	@Getter
	protected AbstractConsumer<?, ?> consumer;

	protected RocketMqConsumerProvider provider;

	protected abstract AbstractConsumer<?, ?> buildConsumer();

	protected abstract void batch(List<RocketMqMessage> messages);

	@Autowired
	public void setProvider(RocketMqConsumerProvider provider) {
		this.provider = provider;
	}

	@Override
	public void onApplicationStart() {
		setConsumer();
		startConsumer();
	}

	protected void setConsumer() {
		if (consumer == null) {
			consumer = buildConsumer();
		}
	}

	@SneakyThrows
	protected void startConsumer() {
		if (!consumer.isStart()) {
			consumer.start(this::batch);
		}
	}

	@Override
	public void onApplicationStop() {
		consumer.stop();
	}

}
