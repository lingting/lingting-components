package live.lingting.component.rocketmq.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;

/**
 * @author lingting 2023-12-06 13:42
 */
@Slf4j
@RequiredArgsConstructor
class RocketMqSendCallbackImpl implements SendCallback {

	private final String group;

	private final String topic;

	private final RocketMqSendCallback callback;

	@Override
	public void onSuccess(SendResult result) {
		final RocketMqSendResult sendResult;
		if (!SendStatus.SEND_OK.equals(result.getSendStatus())) {
			log.warn("消息发送结果状态异常! mqId: {}; status: {}", result.getMsgId(), result.getSendStatus());
			sendResult = RocketMqSendResult.exception(group, topic,
					new MQClientException(result.getSendStatus().name(), null));
		}
		else {
			sendResult = RocketMqSendResult.success(group, topic, result.getMsgId());
		}
		callback.onSuccess(sendResult);
		callback.onFinally();
	}

	@Override
	public void onException(Throwable e) {
		callback.onException(e);
		callback.onFinally();
	}

}
