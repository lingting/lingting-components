package live.lingting.component.rocketmq.producer;

import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.util.unit.DataSize;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-12-09 15:00
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultRocketMqProducerCustomizeImpl implements RocketMqProducerCustomize {

	private final RocketMqProperties properties;

	@Override
	public DefaultMQProducer apply(DefaultMQProducer producer) {
		DataSize maxMessageSize = DataSize.ofMegabytes(20);

		producer.setNamesrvAddr(properties.address());
		producer.setRetryAnotherBrokerWhenNotStoreOK(true);
		producer.setSendMsgTimeout((int) properties.getSendTimeout());
		producer.setMaxMessageSize((int) maxMessageSize.toBytes());
		producer.setMqClientApiTimeout((int) properties.getApiTimeout());
		producer.setEnableStreamRequestType(true);

		ThreadPoolExecutor executor = ThreadPool.instance().getPool();
		producer.setAsyncSenderExecutor(executor);
		return producer;
	}

}
