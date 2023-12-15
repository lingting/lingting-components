package live.lingting.component.configuration;

import live.lingting.component.security.configuration.SecurityAuthorizationConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

/**
 * @author lingting 2023-03-29 21:14
 */
@AutoConfiguration
@ConditionalOnBean(SecurityAuthorizationConfiguration.class)
public class SecurityGrpcAuthorizationAutoConfiguration {

}
