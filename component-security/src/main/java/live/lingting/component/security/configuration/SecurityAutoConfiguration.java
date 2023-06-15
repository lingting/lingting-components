package live.lingting.component.security.configuration;

import live.lingting.component.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lingting 2023-03-29 20:51
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

}
