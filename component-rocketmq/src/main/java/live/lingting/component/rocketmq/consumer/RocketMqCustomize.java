package live.lingting.component.rocketmq.consumer;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;

/**
 * @author lingting 2023-12-09 15:37
 */
public interface RocketMqCustomize {

	void litePull(DefaultLitePullConsumer consumer);

	void push(DefaultMQPushConsumer consumer);

}
