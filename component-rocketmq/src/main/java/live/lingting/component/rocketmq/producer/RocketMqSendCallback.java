package live.lingting.component.rocketmq.producer;

/**
 * @author lingting 2023-05-26 11:09
 */
public interface RocketMqSendCallback {

	void onSuccess(RocketMqSendResult result);

	void onException(Throwable throwable);

	default void onFinally() {
	}

}
