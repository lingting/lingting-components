package live.lingting.component.rocketmq.consumer;

import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.rocketmq.message.RocketMqMessage;
import live.lingting.component.rocketmq.message.RocketMqMessageConsumer;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.store.OffsetStore;
import org.apache.rocketmq.client.consumer.store.ReadOffsetType;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.impl.consumer.DefaultMQPushConsumerImpl;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-11-17 13:34
 */
@Getter
abstract class AbstractPushConsumer<C extends AbstractPushConsumer<C>>
		extends AbstractConsumer<C, DefaultMQPushConsumer> {

	protected final DefaultMQPushConsumer consumer = new DefaultMQPushConsumer();

	protected long consumeTimeout = TimeUnit.MINUTES.toSeconds(10);

	public C consumeTimeout(long consumeTimeout) {
		this.consumeTimeout = consumeTimeout;
		return (C) this;
	}

	@Override
	public C properties(RocketMqProperties properties) {
		return super.properties(properties).consumeTimeout(properties.getConsumeTimeout());
	}

	@Override
	public C start(RocketMqMessageConsumer messageConsumer) throws MQClientException {
		if (!StringUtils.hasText(consumer.getConsumerGroup())) {
			return (C) this;
		}

		consumer.setNamesrvAddr(address);
		consumer.setConsumerGroup(getGroup());
		consumer.setMessageModel(getModel());
		consumer.subscribe(getTopic(), getTags());
		consumer.setConsumeTimeout(getConsumeTimeout());
		consumer.setConsumeMessageBatchMaxSize(getBatchSize());
		consumer.setPullBatchSize(getBatchSize());

		if (messageConsumer != null) {
			consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
				if (!CollectionUtils.isEmpty(list)) {
					List<RocketMqMessage> messages = list.stream().map(this::ofExt).collect(Collectors.toList());
					messageConsumer.batch(messages);
				}
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			});
		}

		if (customize != null) {
			customize.push(consumer);
		}

		consumer.start();
		return (C) this;
	}

	@Override
	public void stop() {
		consumer.shutdown();
	}

	/**
	 * 剩余未读取消息数
	 */
	@Override
	@SneakyThrows
	@SuppressWarnings("java:S1874")
	public long diff() {
		Collection<MessageQueue> queues = consumer.fetchSubscribeMessageQueues(getTopic());

		long diff = 0;

		for (MessageQueue queue : queues) {
			DefaultMQPushConsumerImpl impl = consumer.getDefaultMQPushConsumerImpl();

			OffsetStore store = impl.getOffsetStore();
			// 当前偏移量
			long offset = store.readOffset(queue, ReadOffsetType.READ_FROM_MEMORY);
			// 最大偏移量
			long max = impl.maxOffset(queue);
			diff += max - offset;
		}

		return diff;
	}

	@Override
	public boolean isStart() {
		return StringUtils.hasText(consumer.getNamesrvAddr());
	}

}
