package live.lingting.component.security.web.configuration;

import live.lingting.component.security.authorize.SecurityAuthorizationService;
import live.lingting.component.security.configuration.SecurityAuthorizationConfiguration;
import live.lingting.component.security.password.SecurityPassword;
import live.lingting.component.security.store.SecurityStore;
import live.lingting.component.security.web.endpoint.SecurityWebAuthorizationEndpoint;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:14
 */
@AutoConfiguration
@ConditionalOnBean(SecurityAuthorizationConfiguration.class)
public class SecurityWebAuthorizationAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityWebAuthorizationEndpoint securityWebAuthorizationEndpoint(SecurityAuthorizationService service,
			SecurityStore store, SecurityPassword password) {
		return new SecurityWebAuthorizationEndpoint(service, store, password);
	}

}
