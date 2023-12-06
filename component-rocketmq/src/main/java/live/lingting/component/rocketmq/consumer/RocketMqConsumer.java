package live.lingting.component.rocketmq.consumer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * @author lingting 2023-10-19 15:56
 */
@UtilityClass
public class RocketMqConsumer {

	@NoArgsConstructor(access = AccessLevel.PACKAGE)
	public static class BoardPull extends AbstractPullConsumer<BoardPull> {

		@Override
		public MessageModel getModel() {
			return MessageModel.BROADCASTING;
		}

	}

	@NoArgsConstructor(access = AccessLevel.PACKAGE)
	public static class BoardPush extends AbstractPushConsumer<BoardPush> {

		@Override
		public MessageModel getModel() {
			return MessageModel.BROADCASTING;
		}

	}

	@NoArgsConstructor(access = AccessLevel.PACKAGE)
	public static class ClusterPull extends AbstractPullConsumer<ClusterPull> {

		@Override
		public MessageModel getModel() {
			return MessageModel.CLUSTERING;
		}

	}

	@NoArgsConstructor(access = AccessLevel.PACKAGE)
	public static class ClusterPush extends AbstractPushConsumer<ClusterPush> {

		@Override
		public MessageModel getModel() {
			return MessageModel.CLUSTERING;
		}

	}

}
