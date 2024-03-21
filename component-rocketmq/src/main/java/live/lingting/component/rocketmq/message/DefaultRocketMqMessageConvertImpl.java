package live.lingting.component.rocketmq.message;

import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.NumberUtils;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2023-12-09 15:08
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultRocketMqMessageConvertImpl implements RocketMqMessageConvert {

	public static final String PROPERTY_TIMESTAMP = "timestamp";

	private final RocketMqProperties properties;

	@Override
	public byte[] toBytes(String body) {
		if (body == null) {
			return new byte[0];
		}
		return body.getBytes(properties.getCharset());
	}

	@Override
	public long length(Message message) {
		long length = 0;

		int topic = toBytes(message.getTopic()).length;
		length += topic;

		for (Map.Entry<String, String> entry : message.getProperties().entrySet()) {
			int key = toBytes(entry.getKey()).length;
			int value = toBytes(entry.getValue()).length;

			length = key + value + length;
		}

		// 日志占用预估为 20 字节
		return length + 20;
	}

	@Override
	public List<List<Message>> split(Collection<Message> messages) {
		long maxBytes = properties.getBatchMaxSize().toBytes();

		List<List<Message>> list = new ArrayList<>();
		List<Message> current = new ArrayList<>();
		long length = 0;

		for (Message message : messages) {
			// 当前消息大小
			long l = length(message);

			// 如果加入消息后已超限
			if (l + length >= maxBytes) {
				list.add(current);
				current = new ArrayList<>();
				length = 0;
			}

			// 加入消息
			current.add(message);
			length += l;
		}

		if (!CollectionUtils.isEmpty(current)) {
			list.add(current);
		}

		return list;
	}

	@Override
	public Message of(RocketMqMessage message) {
		return of(message.getTopic(), message.getTags(), message.getKeys(), message.getBody());
	}

	@Override
	public Message of(String topic, String tags, String keys, String body) {
		byte[] bytes = toBytes(body);
		return of(topic, tags, keys, bytes);
	}

	@Override
	public Message of(String topic, String tags, String keys, byte[] body) {
		Message message = new Message(topic, tags, keys, body);
		message.putUserProperty(PROPERTY_TIMESTAMP, Long.toString(System.currentTimeMillis()));
		return message;
	}

	@Override
	public RocketMqMessage of(String group, MessageExt ext) {
		String id = ext.getMsgId();
		String topic = ext.getTopic();
		String tags = ext.getTags();
		String keys = ext.getKeys();
		byte[] bytes = ext.getBody();
		String body = new String(bytes, properties.getCharset());
		Number number = NumberUtils.toNumber(ext.getUserProperty(PROPERTY_TIMESTAMP));
		Long timestamp = number == null ? null : number.longValue();
		return new RocketMqMessage(id, group, topic, tags, keys, body, timestamp, ext.getDelayTimeLevel());
	}

}
