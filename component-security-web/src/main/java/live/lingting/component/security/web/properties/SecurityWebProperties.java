package live.lingting.component.security.web.properties;

import live.lingting.component.security.properties.SecurityProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author lingting 2023-03-29 21:31
 */
@Data
@ConfigurationProperties(SecurityWebProperties.PREFIX)
public class SecurityWebProperties {

	public static final String PREFIX = SecurityProperties.PREFIX + ".web";

	private Set<String> ignoreAntPatterns;

}
