package live.lingting.component.security.configuration;

import live.lingting.component.security.authorize.SecurityAuthorize;
import live.lingting.component.security.properties.SecurityProperties;
import live.lingting.component.security.resource.SecurityDefaultResourceServiceImpl;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.store.SecurityStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:09
 */
public class SecurityResourceConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityAuthorize securityAuthorize() {
		return new SecurityAuthorize();
	}

	/**
	 * 使用本地解析token
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX + ".authorization", value = "remote",
			havingValue = "false", matchIfMissing = true)
	public SecurityResourceService securityStoreResourceService(SecurityStore store) {
		return new SecurityDefaultResourceServiceImpl(store);
	}

}
