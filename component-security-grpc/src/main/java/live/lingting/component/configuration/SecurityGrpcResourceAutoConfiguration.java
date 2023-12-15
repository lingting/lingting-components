package live.lingting.component.configuration;

import live.lingting.component.exception.SecurityGrpcExceptionHandler;
import live.lingting.component.grpc.server.GrpcServer;
import live.lingting.component.interceptor.SecurityGrpcResourceInterceptor;
import live.lingting.component.properties.SecurityGrpcProperties;
import live.lingting.component.security.authorize.SecurityAuthorize;
import live.lingting.component.security.configuration.SecurityResourceConfiguration;
import live.lingting.component.security.resource.SecurityResourceService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:14
 */
@ConditionalOnBean(SecurityResourceConfiguration.class)
@AutoConfiguration(beforeName = { "SecurityWebResourceAutoConfiguration" })
public class SecurityGrpcResourceAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(GrpcServer.class)
	public SecurityGrpcResourceInterceptor securityGrpcResourceInterceptor(SecurityGrpcProperties properties,
			SecurityResourceService service, SecurityAuthorize authorize,
			SecurityGrpcExceptionHandler exceptionHandler) {
		return new SecurityGrpcResourceInterceptor(properties, service, authorize, exceptionHandler);
	}

}
