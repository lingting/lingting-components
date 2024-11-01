package live.lingting.component.configuration;

import live.lingting.component.convert.SecurityGrpcConvert;
import live.lingting.component.convert.SecurityGrpcDefaultConvert;
import live.lingting.component.exception.SecurityGrpcExceptionHandler;
import live.lingting.component.interceptor.SecurityGrpcClientInterceptor;
import live.lingting.component.properties.SecurityGrpcProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-12-14 15:52
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityGrpcProperties.class)
public class SecurityGrpcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcExceptionHandler securityGrpcExceptionHandler() {
		return new SecurityGrpcExceptionHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcConvert securityGrpcConvert() {
		return new SecurityGrpcDefaultConvert();
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcClientInterceptor securityGrpcClientInterceptor(SecurityGrpcProperties properties) {
		return new SecurityGrpcClientInterceptor(properties.authorizationKey());
	}

}
