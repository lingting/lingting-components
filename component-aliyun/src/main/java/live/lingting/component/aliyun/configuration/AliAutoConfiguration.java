package live.lingting.component.aliyun.configuration;

import live.lingting.component.aliyun.AliOss;
import live.lingting.component.aliyun.AliSts;
import live.lingting.component.aliyun.oss.AliOssBucket;
import live.lingting.component.aliyun.proerties.AliOssProperties;
import live.lingting.component.aliyun.proerties.AliStsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-21 14:24
 */
@AutoConfiguration
@EnableConfigurationProperties({ AliOssProperties.class, AliStsProperties.class })
public class AliAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = AliStsProperties.PREFIX, name = "access-key")
	public AliSts aliSts(AliStsProperties properties) {
		return new AliSts(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = AliOssProperties.PREFIX, name = "access-key")
	public AliOss aliOss(AliOssProperties properties) {
		return new AliOss(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(AliOss.class)
	@ConditionalOnProperty(prefix = AliOssProperties.PREFIX, name = "bucket")
	public AliOssBucket aliOss(AliOssProperties properties, AliOss oss) {
		return oss.bucket(properties.getBucket());
	}

}
