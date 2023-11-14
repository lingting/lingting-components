package live.lingting.component.aliyun.proerties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2023-04-21 14:18
 */
@Getter
@Setter
@ConfigurationProperties(AliStsProperties.PREFIX)
public class AliStsProperties extends AbstractProperties {

	public static final String PREFIX = "lingting.aliyun.sts";

	/**
	 * 默认sts过期时间, 单位: 秒
	 */
	private Long durationSeconds = 3600L;

	private String roleArn;

	private String roleSessionName;

}
