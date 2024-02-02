package live.lingting.component.rocketmq.message;

import live.lingting.component.rocketmq.RocketMqTarget;
import lombok.Getter;

/**
 * @author lingting 2023-05-26 10:07
 */
@Getter
@SuppressWarnings("java:S107")
public class RocketMqMessage {

	/**
	 * 发送消息此值为null
	 */
	private final String id;

	private final String group;

	private final String topic;

	private final String tags;

	private final String keys;

	private final String body;

	private final Long sendTimestamp;

	private final int delayTimeLevel;

	public RocketMqMessage(String id, String group, String topic, String tags, String keys, String body,
			Long sendTimestamp, int delayTimeLevel) {
		this.id = id;
		this.group = group;
		this.topic = topic;
		this.tags = tags;
		this.keys = keys;
		// rocket mq 消息体不允许为空字节
		this.body = body == null || body.isEmpty() ? " " : body;
		this.sendTimestamp = sendTimestamp;
		this.delayTimeLevel = delayTimeLevel;
	}

	public static RocketMqMessage send(RocketMqTarget target, String tags, String keys, String body) {
		return send(target.getGroup(), target.getTopic(), tags, keys, body);
	}

	public static RocketMqMessage send(String group, String topic, String tags, String keys, String body) {
		return new RocketMqMessage("", group, topic, tags, keys, body, null, 0);
	}

	public RocketMqMessage delay(int delayTimeLevel) {
		return new RocketMqMessage(id, group, topic, tags, keys, body, sendTimestamp, delayTimeLevel);
	}

}
