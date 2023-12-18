package live.lingting.component.configuration;

import live.lingting.component.convert.SecurityGrpcConvert;
import live.lingting.component.endpoint.SecurityGrpcAuthorizationEndpoint;
import live.lingting.component.security.authorize.SecurityAuthorizationService;
import live.lingting.component.security.configuration.SecurityAuthorizationConfiguration;
import live.lingting.component.security.password.SecurityPassword;
import live.lingting.component.security.store.SecurityStore;
import live.lingting.protobuf.SecurityGrpcAuthorizationServiceGrpc;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:14
 */
@AutoConfiguration
@ConditionalOnBean(SecurityAuthorizationConfiguration.class)
public class SecurityGrpcAuthorizationAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityGrpcAuthorizationServiceGrpc.SecurityGrpcAuthorizationServiceImplBase securityGrpcAuthorizationServiceImplBase(
			SecurityAuthorizationService service, SecurityStore store, SecurityPassword password,
			SecurityGrpcConvert convert) {
		return new SecurityGrpcAuthorizationEndpoint(service, store, password, convert);
	}

}
