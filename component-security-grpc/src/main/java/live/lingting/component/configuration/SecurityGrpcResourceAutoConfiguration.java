package live.lingting.component.configuration;

import live.lingting.component.convert.SecurityGrpcConvert;
import live.lingting.component.exception.SecurityGrpcExceptionHandler;
import live.lingting.component.grpc.client.GrpcClientProvide;
import live.lingting.component.grpc.client.configuration.GrpcClientAutoConfiguration;
import live.lingting.component.interceptor.SecurityGrpcResourceServerInterceptor;
import live.lingting.component.properties.SecurityGrpcProperties;
import live.lingting.component.resource.SecurityGrpcDefaultRemoteResourceServiceImpl;
import live.lingting.component.security.authorize.SecurityAuthorize;
import live.lingting.component.security.configuration.SecurityResourceConfiguration;
import live.lingting.component.security.properties.SecurityProperties;
import live.lingting.component.security.resource.SecurityResourceService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:14
 */
@ConditionalOnBean(SecurityResourceConfiguration.class)
@AutoConfiguration(beforeName = { "SecurityWebResourceAutoConfiguration" }, after = GrpcClientAutoConfiguration.class)
public class SecurityGrpcResourceAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcResourceServerInterceptor securityGrpcResourceServerInterceptor(
			SecurityGrpcProperties properties, SecurityResourceService service, SecurityAuthorize authorize,
			SecurityGrpcExceptionHandler exceptionHandler) {
		return new SecurityGrpcResourceServerInterceptor(properties, service, authorize, exceptionHandler);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(GrpcClientProvide.class)
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX + ".authorization", value = "remote",
			havingValue = "true")
	public SecurityResourceService serviceGrpcRemoteResourceService(SecurityProperties properties,
			GrpcClientProvide provide, SecurityGrpcConvert convert) {
		return new SecurityGrpcDefaultRemoteResourceServiceImpl(properties, provide, convert);
	}

}
