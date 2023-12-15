package live.lingting.component.properties;

import live.lingting.component.security.properties.SecurityProperties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author lingting 2023-12-14 16:40
 */
@Data
@ConfigurationProperties(SecurityGrpcProperties.PREFIX)
public class SecurityGrpcProperties {

	public static final String PREFIX = SecurityProperties.PREFIX + ".web";

	private Set<String> ignoreAntPatterns;

	private String authorizationKey = "Authorization";

}
