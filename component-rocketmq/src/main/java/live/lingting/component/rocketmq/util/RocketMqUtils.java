package live.lingting.component.rocketmq.util;

import live.lingting.component.rocketmq.RocketMqMessage;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import live.lingting.component.core.util.CollectionUtils;
import lombok.experimental.UtilityClass;
import org.apache.rocketmq.client.ClientConfig;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.unit.DataSize;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2023-12-06 18:59
 */
@UtilityClass
public class RocketMqUtils {

	public static ClientConfig toConfig(RocketMqProperties properties) {
		ClientConfig clientConfig = new ClientConfig();
		clientConfig.setNamesrvAddr(properties.address());
		clientConfig.setMqClientApiTimeout((int) properties.getApiTimeout());
		return clientConfig;
	}

	public static RocketMqMessage ofExt(String group, MessageExt ext, Charset charset) {
		String extTopic = ext.getTopic();
		String extTags = ext.getTags();
		byte[] bytes = ext.getBody();
		String body = new String(bytes, charset);

		return new RocketMqMessage(group, extTopic, extTags, ext.getKeys(), body);
	}

	public static byte[] toBytes(RocketMqMessage message, Charset charset) {
		return toBytes(message.getBody(), charset);
	}

	public static byte[] toBytes(String body, Charset charset) {
		if (body == null) {
			return new byte[0];
		}
		return body.getBytes(charset);
	}

	public static Message toMessage(RocketMqMessage message, Charset charset) {
		byte[] body = toBytes(message, charset);
		return toMessage(message.getTopic(), message.getTags(), message.getKeys(), body);
	}

	public static Message toMessage(String topic, String tags, String keys, byte[] body) {
		return new Message(topic, tags, keys, body);
	}

	/**
	 * 计算指定消息占用多少字节
	 */
	public static long length(Message message, Charset charset) {
		long length = 0;

		int topic = toBytes(message.getTopic(), charset).length;
		length += topic;

		for (Map.Entry<String, String> entry : message.getProperties().entrySet()) {
			int key = toBytes(entry.getKey(), charset).length;
			int value = toBytes(entry.getValue(), charset).length;

			length = key + value + length;
		}

		// 日志占用预估为 20 字节
		return length + 20;
	}

	/**
	 * 按照设置的批量消息大小最大值进行分割
	 * @param messages 消息队列
	 * @return 分割后的消息组合
	 */
	public static List<List<Message>> split(Collection<Message> messages, DataSize maxSize, Charset charset) {
		long maxBytes = maxSize.toBytes();

		List<List<Message>> list = new ArrayList<>();
		List<Message> current = new ArrayList<>();
		long length = 0;

		for (Message message : messages) {
			// 当前消息大小
			long l = length(message, charset);

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

}
