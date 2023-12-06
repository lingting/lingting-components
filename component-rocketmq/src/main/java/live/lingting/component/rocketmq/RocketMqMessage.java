package live.lingting.component.rocketmq;

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

	public RocketMqMessage(String group, String topic, String tags, String keys, String body) {
		this.group = group;
		this.topic = topic;
		this.tags = tags;
		this.keys = keys;
		// rocket mq 消息体不允许为空字节
		this.body = body == null || body.isEmpty() ? " " : body;
	}

	public RocketMqMessage(RocketMqTarget target, String tags, String keys, String body) {
		this(target.getGroup(), target.getTopic(), tags, keys, body);
	}

}
