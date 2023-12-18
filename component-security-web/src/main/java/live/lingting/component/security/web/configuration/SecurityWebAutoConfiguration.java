package live.lingting.component.security.web.configuration;

import live.lingting.component.security.web.exception.SecurityWebExceptionHandler;
import live.lingting.component.security.web.properties.SecurityWebProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-03-29 21:13
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityWebProperties.class)
public class SecurityWebAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public SecurityWebExceptionHandler securityWebExceptionHandler() {
		return new SecurityWebExceptionHandler();
	}

}
