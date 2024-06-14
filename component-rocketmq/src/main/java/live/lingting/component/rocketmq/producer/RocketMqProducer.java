package live.lingting.component.rocketmq.producer;

import live.lingting.component.core.context.ContextComponent;
import live.lingting.component.core.function.ThrowingSupplier;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.rocketmq.RocketMqTarget;
import live.lingting.component.rocketmq.message.RocketMqMessage;
import live.lingting.component.rocketmq.message.RocketMqMessageConvert;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-05-26 09:42
 */
@Slf4j
@RequiredArgsConstructor
public class RocketMqProducer implements ContextComponent {

	private final RocketMqProducerCustomize customize;

	private final RocketMqMessageConvert convert;

	private final Map<String, DefaultMQProducer> producerMap = new ConcurrentHashMap<>();

	// region 同步发送
	/**
	 * 发送消息
	 * @param target target
	 * @param keys 消息key
	 * @param body 消息内容
	 */
	public RocketMqSendResult send(RocketMqTarget target, String keys, String body) {
		return send(RocketMqMessage.send(target, null, keys, body));
	}

	public RocketMqSendResult send(RocketMqTarget target, String tags, String keys, String body) {
		return send(RocketMqMessage.send(target, tags, keys, body));
	}

	public RocketMqSendResult send(String group, String topic, String tags, String keys, byte[] body) {
		Message message = convert.of(topic, tags, keys, body);
		return send(group, message);
	}

	public RocketMqSendResult send(RocketMqMessage message) {
		return send(message.getGroup(), convert.of(message));
	}

	RocketMqSendResult send(String group, Message message) {
		String topic = message.getTopic();
		try {
			DefaultMQProducer producer = producer(group);
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
		send(RocketMqMessage.send(target, null, keys, body), callback);
	}

	public void send(RocketMqTarget target, String tags, String keys, String body, RocketMqSendCallback callback) {
		send(RocketMqMessage.send(target, tags, keys, body), callback);
	}

	public void send(RocketMqMessage message, RocketMqSendCallback callback) {
		byte[] bytes = convert.toBytes(message);
		send(message.getGroup(), message.getTopic(), message.getTags(), message.getKeys(), bytes, callback);
	}

	public void send(String group, String topic, String tags, String keys, byte[] body, RocketMqSendCallback callback) {
		Message message = convert.of(topic, tags, keys, body);
		send(group, topic, () -> Collections.singletonList(message), callback);
	}

	public void send(RocketMqTarget target, Collection<RocketMqMessage> messages, RocketMqSendCallback callback) {
		send(target.getGroup(), target.getTopic(), messages, callback);
	}

	public void send(String group, String topic, Collection<RocketMqMessage> messages, RocketMqSendCallback callback) {
		send(group, topic,
				() -> messages.stream().filter(Objects::nonNull).map(convert::of).collect(Collectors.toList()),
				callback);
	}

	void send(String group, String topic, ThrowingSupplier<List<Message>> supplier, RocketMqSendCallback callback) {
		try {
			DefaultMQProducer producer = producer(group);
			List<Message> rawMessages = supplier.get();
			List<List<Message>> split = convert.split(rawMessages);

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
		sendOneway(RocketMqMessage.send(target, null, keys, body));
	}

	public void sendOneway(RocketMqTarget target, String tags, String keys, String body) {
		sendOneway(RocketMqMessage.send(target, tags, keys, body));
	}

	public void sendOneway(String group, String topic, String tags, String keys, byte[] body) {
		Message message = convert.of(topic, tags, keys, body);
		sendOneway(group, message);
	}

	public void sendOneway(RocketMqMessage message) {
		sendOneway(message.getGroup(), convert.of(message));
	}

	void sendOneway(String group, Message message) {
		String topic = message.getTopic();
		try {
			DefaultMQProducer producer = producer(group);
			producer.sendOneway(message);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		catch (Exception e) {
			log.error("mq单向消息发送异常! topic: {}", topic, e);
		}
	}
	// endregion

	// region 其他

	public DefaultMQProducer producer(RocketMqTarget target) {
		return producer(target.getGroup());
	}

	public DefaultMQProducer producer(String group) {
		return producerMap.computeIfAbsent(group, new Function<String, DefaultMQProducer>() {
			@SneakyThrows
			@Override
			public DefaultMQProducer apply(String k) {
				DefaultMQProducer producer = customize.apply(new DefaultMQProducer(group));
				producer.start();
				return producer;
			}
		});
	}

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

	// endregion

}
