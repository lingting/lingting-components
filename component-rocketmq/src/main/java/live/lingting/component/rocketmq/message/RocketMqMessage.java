package live.lingting.component.rocketmq.message;

import live.lingting.component.rocketmq.RocketMqTarget;
import lombok.Getter;

/**
 * @author lingting 2023-05-26 10:07
 */
@Getter
public class RocketMqMessage {

	private final String group;

	private final String topic;

	private final String tags;

	private final String keys;

	private final String body;

	private final Long sendTimestamp;

	private RocketMqMessage(String group, String topic, String tags, String keys, String body, Long sendTimestamp) {
		this.group = group;
		this.topic = topic;
		this.tags = tags;
		this.keys = keys;
		// rocket mq 消息体不允许为空字节
		this.body = body == null || body.isEmpty() ? " " : body;
		this.sendTimestamp = sendTimestamp;
	}

	public static RocketMqMessage send(RocketMqTarget target, String tags, String keys, String body) {
		return send(target.getGroup(), target.getTopic(), tags, keys, body);
	}

	public static RocketMqMessage send(String group, String topic, String tags, String keys, String body) {
		return new RocketMqMessage(group, topic, tags, keys, body, null);
	}

	public static RocketMqMessage receive(String group, String topic, String tags, String keys, String body,
			Long sendTimestamp) {
		return new RocketMqMessage(group, topic, tags, keys, body, sendTimestamp);
	}

}
