package live.lingting.component.rocketmq.configuration;

import live.lingting.component.rocketmq.consumer.RocketMqConsumerProvider;
import live.lingting.component.rocketmq.message.DefaultRocketMqMessageConvertImpl;
import live.lingting.component.rocketmq.message.RocketMqMessageConvert;
import live.lingting.component.rocketmq.producer.DefaultRocketMqProducerCustomizeImpl;
import live.lingting.component.rocketmq.producer.RocketMqProducer;
import live.lingting.component.rocketmq.producer.RocketMqProducerCustomize;
import live.lingting.component.rocketmq.properties.RocketMqProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
	public RocketMqMessageConvert rocketMqMessageConvert(RocketMqProperties properties) {
		return new DefaultRocketMqMessageConvertImpl(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = RocketMqProperties.PREFIX, name = "host")
	public RocketMqProducerCustomize rocketMqProducerCustomize(RocketMqProperties properties) {
		return new DefaultRocketMqProducerCustomizeImpl(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(RocketMqProducerCustomize.class)
	@ConditionalOnProperty(prefix = RocketMqProperties.PREFIX, name = "host")
	public RocketMqProducer rocketMqProducer(RocketMqProducerCustomize customize, RocketMqMessageConvert convert) {
		return new RocketMqProducer(customize, convert);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = RocketMqProperties.PREFIX, name = "host")
	public RocketMqConsumerProvider rocketMqConsumerProvider(RocketMqProperties properties,
			RocketMqMessageConvert convert) {
		return new RocketMqConsumerProvider(properties, convert);
	}

}
