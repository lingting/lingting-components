package live.lingting.component.rocketmq.producer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lingting 2023-05-26 10:14
 */
@Getter
@RequiredArgsConstructor
public class RocketMqSendResult {

	private final String group;

	private final String topic;

	private final String messageId;

	private final Exception exception;

	private boolean success = false;

	public static RocketMqSendResult exception(String group, String topic, Exception e) {
		return new RocketMqSendResult(group, topic, null, e);
	}

	public static RocketMqSendResult success(String group, String topic, String messageId) {
		RocketMqSendResult result = new RocketMqSendResult(group, topic, messageId, null);
		result.success = true;
		return result;
	}

}
