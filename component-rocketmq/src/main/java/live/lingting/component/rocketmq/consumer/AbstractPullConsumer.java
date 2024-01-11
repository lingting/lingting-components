package live.lingting.component.rocketmq.consumer;

import live.lingting.component.core.domain.ClassField;
import live.lingting.component.core.util.ClassUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.rocketmq.message.RocketMqMessageConsumer;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.client.consumer.store.ReadOffsetType;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.consumer.DefaultLitePullConsumerImpl;
import org.apache.rocketmq.common.message.MessageQueue;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-11-17 13:34
 */
@Getter
@SuppressWarnings("unchecked")
abstract class AbstractPullConsumer<C extends AbstractPullConsumer<C>>
		extends AbstractConsumer<C, DefaultLitePullConsumer> {

	protected final DefaultLitePullConsumer consumer = new DefaultLitePullConsumer();

	protected final DefaultLitePullConsumerImpl consumerImpl = getImpl(consumer);

	protected boolean autoCommit = true;

	/**
	 * 拉取消息超时时间, 单位: 毫秒
	 */
	protected long pullTimeout = TimeUnit.SECONDS.toMillis(1);

	/**
	 * 在未拉取到消息时, 间隔多久再次拉取, 单位: 毫秒
	 */
	protected long pullInterval = TimeUnit.SECONDS.toMillis(1);

	public C autoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
		return (C) this;
	}

	public C pullTimeout(long value) {
		pullTimeout = value;
		return (C) this;
	}

	public C pullInterval(long value) {
		pullInterval = value;
		return (C) this;
	}

	@Override
	public C properties(RocketMqProperties properties) {
		return super.properties(properties).pullTimeout(properties.getPullTimeout())
			.pullInterval(properties.getPullInterval());
	}

	@Override
	public C start(RocketMqMessageConsumer messageConsumer) throws MQClientException {
		if (consumer.isRunning()) {
			return (C) this;
		}

		consumer.setNamesrvAddr(getAddress());
		consumer.setConsumerGroup(getGroup());
		consumer.setMessageModel(getModel());
		consumer.subscribe(getTopic(), getTags());
		consumer.setPollTimeoutMillis(getPullTimeout());
		consumer.setAutoCommit(isAutoCommit());
		consumer.setPullBatchSize(getBatchSize());

		if (customize != null) {
			customize.litePull(consumer);
		}

		consumer.start();

		RocketMqConsumerTimer timer = new RocketMqConsumerTimer(this, messageConsumer);
		timer.onApplicationStart();
		return (C) this;
	}

	@Override
	public void stop() {
		consumer.shutdown();
	}

	@SneakyThrows
	protected DefaultLitePullConsumerImpl getImpl(DefaultLitePullConsumer consumer) {
		ClassField cf = ClassUtils.classField("defaultLitePullConsumerImpl", DefaultLitePullConsumer.class);
		return (DefaultLitePullConsumerImpl) cf.get(consumer);
	}

	/**
	 * 剩余未读取消息数
	 */
	@Override
	@SneakyThrows
	public long diff() {
		Collection<MessageQueue> queues = consumer.fetchMessageQueues(getTopic());

		long diff = 0;

		for (MessageQueue queue : queues) {
			OffsetStore store = consumer.getOffsetStore();
			// 当前偏移量
			long offset = store.readOffset(queue, ReadOffsetType.READ_FROM_MEMORY);
			Method method = ClassUtils.method(DefaultLitePullConsumerImpl.class, "maxOffset", MessageQueue.class);
			// 最大偏移量
			long max = (long) method.invoke(consumerImpl, queue);
			diff += max - offset;
		}

		return diff;
	}

	@Override
	public boolean isStart() {
		return StringUtils.hasText(consumer.getNamesrvAddr()) || consumer.isRunning();
	}

}
