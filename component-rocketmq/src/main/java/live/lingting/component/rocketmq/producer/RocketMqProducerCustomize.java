package live.lingting.component.rocketmq.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 * @author lingting 2023-12-09 14:59
 */
public interface RocketMqProducerCustomize {

	DefaultMQProducer apply(DefaultMQProducer producer);

}
