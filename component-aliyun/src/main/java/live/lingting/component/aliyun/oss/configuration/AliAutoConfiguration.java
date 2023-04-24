package live.lingting.component.aliyun.oss.configuration;

import live.lingting.component.aliyun.oss.AliOss;
import live.lingting.component.aliyun.oss.AliSts;
import live.lingting.component.aliyun.oss.proerties.AliOssProperties;
import live.lingting.component.aliyun.oss.proerties.AliStsProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
	public AliSts aliSts(AliStsProperties properties) throws Exception {
		return new AliSts(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public AliOss aliOss(AliOssProperties properties, AliSts sts) {
		return new AliOss(properties, sts);
	}

}
