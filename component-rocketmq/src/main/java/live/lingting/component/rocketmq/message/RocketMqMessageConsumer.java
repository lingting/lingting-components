package live.lingting.component.rocketmq.message;

import java.util.List;

/**
 * @author lingting 2023-05-26 13:58
 */
public interface RocketMqMessageConsumer {

	void batch(List<RocketMqMessage> message);

}
