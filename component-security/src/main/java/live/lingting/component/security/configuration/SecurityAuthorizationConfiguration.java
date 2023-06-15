package live.lingting.component.security.configuration;

import live.lingting.component.security.password.SecurityDefaultPassword;
import live.lingting.component.security.password.SecurityPassword;
import live.lingting.component.security.properties.SecurityProperties;
import live.lingting.component.security.store.SecurityMemoryStore;
import live.lingting.component.security.store.SecurityStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-06 15:56
 */
public class SecurityAuthorizationConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityStore securityStore() {
		return new SecurityMemoryStore();
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityPassword securityPassword(SecurityProperties properties) {
		SecurityProperties.Authorization authorization = properties.getAuthorization();
		return new SecurityDefaultPassword(authorization.getPasswordSecretKey());
	}

}
