package live.lingting.component.rocketmq.configuration;

import live.lingting.component.rocketmq.consumer.RocketMqConsumerProvider;
import live.lingting.component.rocketmq.producer.RocketMqProducer;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-05-26 09:48
 */
@AutoConfiguration
@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = RocketMqProperties.PREFIX, name = "host")
	public RocketMqProducer rocketMqProducer(RocketMqProperties properties) {
		return new RocketMqProducer(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = RocketMqProperties.PREFIX, name = "host")
	public RocketMqConsumerProvider rocketMqConsumerProvider(RocketMqProperties properties) {
		return new RocketMqConsumerProvider(properties);
	}

}
