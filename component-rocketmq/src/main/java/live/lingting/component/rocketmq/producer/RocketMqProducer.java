package live.lingting.component.rocketmq.producer;

import live.lingting.component.core.context.ContextComponent;
import live.lingting.component.core.function.ThrowingSupplier;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.rocketmq.RocketMqMessage;
import live.lingting.component.rocketmq.RocketMqTarget;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import live.lingting.component.rocketmq.util.RocketMqUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.util.unit.DataSize;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-05-26 09:42
 */
@Slf4j
public class RocketMqProducer implements ContextComponent {

	private final RocketMqProperties properties;

	private final Map<String, DefaultMQProducer> producerMap = new ConcurrentHashMap<>();

	public RocketMqProducer(RocketMqProperties properties) {
		this.properties = properties;
	}

	DefaultMQProducer producer(String group) {
		return producerMap.computeIfAbsent(group, k -> createProducer(group));
	}

	@SneakyThrows
	DefaultMQProducer createProducer(String group) {
		DataSize maxMessageSize = DataSize.ofMegabytes(20);

		DefaultMQProducer producer = new DefaultMQProducer();
		producer.setNamesrvAddr(properties.address());
		producer.setProducerGroup(group);
		producer.setRetryAnotherBrokerWhenNotStoreOK(true);
		producer.setSendMsgTimeout((int) properties.getSendTimeout());
		producer.setMaxMessageSize((int) maxMessageSize.toBytes());
		producer.setMqClientApiTimeout((int) properties.getApiTimeout());
		producer.start();
		return producer;
	}

	public byte[] toBytes(RocketMqMessage message) {
		return toBytes(message.getBody());
	}

	public byte[] toBytes(String body) {
		return RocketMqUtils.toBytes(body, properties.getCharset());
	}

	protected Message toMessage(RocketMqMessage message) {
		return RocketMqUtils.toMessage(message, properties.getCharset());
	}

	protected Message toMessage(String topic, String tags, String keys, byte[] body) {
		return new Message(topic, tags, keys, body);
	}

	/**
	 * 按照设置的批量消息大小最大值进行分割
	 * @param messages 消息队列
	 * @return 分割后的消息组合
	 */
	protected List<List<Message>> split(Collection<Message> messages) {
		return RocketMqUtils.split(messages, properties.getBatchMaxSize(), properties.getCharset());
	}

	// region 同步发送
	/**
	 * 发送消息
	 * @param target target
	 * @param keys 消息key
	 * @param body 消息内容
	 */
	public RocketMqSendResult send(RocketMqTarget target, String keys, String body) {
		return send(target, null, keys, body);
	}

	public RocketMqSendResult send(RocketMqTarget target, String tags, String keys, String body) {
		return send(new RocketMqMessage(target.getGroup(), target.getTopic(), tags, keys, body));
	}

	public RocketMqSendResult send(RocketMqMessage message) {
		byte[] bytes = toBytes(message);
		return send(message.getGroup(), message.getTopic(), message.getTags(), message.getKeys(), bytes);
	}

	public RocketMqSendResult send(String group, String topic, String tags, String keys, byte[] body) {
		try {
			DefaultMQProducer producer = producer(group);
			Message message = toMessage(topic, tags, keys, body);
			SendResult result = producer.send(message);
			if (!SendStatus.SEND_OK.equals(result.getSendStatus())) {
				log.warn("消息发送结果状态异常! mqId: {}; status: {}", result.getMsgId(), result.getSendStatus());
				return RocketMqSendResult.exception(group, topic,
						new MQClientException(result.getSendStatus().name(), null));
			}
			return RocketMqSendResult.success(group, topic, result.getMsgId());
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			return RocketMqSendResult.exception(group, topic, e);
		}
		catch (Exception e) {
			log.error("mq消息同步发送异常!", e);
			return RocketMqSendResult.exception(group, topic, e);
		}
	}
	// endregion

	// region 异步发送
	public void send(RocketMqTarget target, String keys, String body, RocketMqSendCallback callback) {
		send(target, null, keys, body, callback);
	}

	public void send(RocketMqTarget target, String tags, String keys, String body, RocketMqSendCallback callback) {
		send(new RocketMqMessage(target.getGroup(), target.getTopic(), tags, keys, body), callback);
	}

	public void send(RocketMqMessage message, RocketMqSendCallback callback) {
		byte[] bytes = toBytes(message);
		send(message.getGroup(), message.getTopic(), message.getTags(), message.getKeys(), bytes, callback);
	}

	public void send(String group, String topic, String tags, String keys, byte[] body, RocketMqSendCallback callback) {
		Message message = toMessage(topic, tags, keys, body);
		send(group, topic, () -> Collections.singletonList(message), callback);
	}

	public void send(RocketMqTarget target, Collection<RocketMqMessage> messages, RocketMqSendCallback callback) {
		send(target.getGroup(), target.getTopic(), messages, callback);
	}

	public void send(String group, String topic, Collection<RocketMqMessage> messages, RocketMqSendCallback callback) {
		send(group, topic,
				() -> messages.stream().filter(Objects::nonNull).map(this::toMessage).collect(Collectors.toList()),
				callback);
	}

	void send(String group, String topic, ThrowingSupplier<List<Message>> supplier, RocketMqSendCallback callback) {
		try {
			DefaultMQProducer producer = producer(group);
			List<Message> rawMessages = supplier.get();
			List<List<Message>> split = split(rawMessages);

			for (List<Message> messages : split) {
				if (CollectionUtils.isEmpty(messages)) {
					return;
				}
				RocketMqSendCallbackImpl impl = new RocketMqSendCallbackImpl(group, topic, callback);

				if (messages.size() == 1) {
					producer.send(messages.get(0), impl);
				}
				else {
					producer.send(messages, impl);
				}
			}
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			callback.onException(e);
		}
		catch (Exception e) {
			log.error("mq消息异步发送异常!", e);
			callback.onException(e);
		}
	}

	// endregion

	// region 单向发送
	/**
	 * 发送消息
	 * @param target target
	 * @param keys 消息key
	 * @param body 消息内容
	 */
	public void sendOneway(RocketMqTarget target, String keys, String body) {
		sendOneway(target, null, keys, body);
	}

	public void sendOneway(RocketMqTarget target, String tags, String keys, String body) {
		sendOneway(new RocketMqMessage(target.getGroup(), target.getTopic(), tags, keys, body));
	}

	public void sendOneway(RocketMqMessage message) {
		byte[] bytes = toBytes(message);
		sendOneway(message.getGroup(), message.getTopic(), message.getTags(), message.getKeys(), bytes);
	}

	public void sendOneway(String group, String topic, String tags, String keys, byte[] body) {
		try {
			DefaultMQProducer producer = producer(group);
			Message message = toMessage(topic, tags, keys, body);
			producer.sendOneway(message);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		catch (Exception e) {
			log.error("mq单向消息发送异常!", e);
		}
	}
	// endregion

	@Override
	public void onApplicationStart() {
		//
	}

	@Override
	public void onApplicationStop() {
		for (DefaultMQProducer producer : producerMap.values()) {
			producer.shutdown();
		}
	}

}
