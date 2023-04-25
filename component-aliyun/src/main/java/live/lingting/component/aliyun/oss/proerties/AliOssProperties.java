package live.lingting.component.aliyun.oss.proerties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2023-04-21 14:18
 */
@Data
@ConfigurationProperties(AliOssProperties.PREFIX)
public class AliOssProperties {

	public static final String PREFIX = "lingting.aliyun.oss";

	private String region;

	private String accessKey;

	private String accessSecret;

	private String bucket;

}
