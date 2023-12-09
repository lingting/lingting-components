package live.lingting.component.rocketmq.message;

import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Collection;
import java.util.List;

/**
 * @author lingting 2023-12-09 15:06
 */
public interface RocketMqMessageConvert {

	default byte[] toBytes(RocketMqMessage message) {
		return toBytes(message.getBody());
	}

	byte[] toBytes(String body);

	/**
	 * 计算指定消息占用多少字节
	 */
	long length(Message message);

	/**
	 * 按照设置的批量消息大小最大值进行分割
	 * @param messages 消息队列
	 * @return 分割后的消息组合
	 */
	List<List<Message>> split(Collection<Message> messages);

	Message of(RocketMqMessage rawMessage);

	Message of(String topic, String tags, String keys, String body);

	Message of(String topic, String tags, String keys, byte[] body);

	RocketMqMessage of(String group, MessageExt ext);

}
