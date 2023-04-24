package live.lingting.component.aliyun.oss.proerties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2023-04-21 14:18
 */
@Data
@ConfigurationProperties(AliStsProperties.PREFIX)
public class AliStsProperties {

	public static final String PREFIX = "lingting.ali.sts";

	private String protocol = "https";

	private Long durationSeconds = 3600L;

	private String region;

	private String accessKey;

	private String accessSecret;

	private String roleArn;

	private String roleSessionName;

}
