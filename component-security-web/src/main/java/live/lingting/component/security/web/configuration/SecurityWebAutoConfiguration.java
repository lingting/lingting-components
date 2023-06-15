package live.lingting.component.security.web.configuration;

import live.lingting.component.security.web.properties.SecurityWebProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lingting 2023-03-29 21:13
 */
@AutoConfiguration
@ComponentScan({ "live.lingting.component.security.web.endpoint", "live.lingting.component.security.web.exception" })
@EnableConfigurationProperties(SecurityWebProperties.class)
public class SecurityWebAutoConfiguration {

}
