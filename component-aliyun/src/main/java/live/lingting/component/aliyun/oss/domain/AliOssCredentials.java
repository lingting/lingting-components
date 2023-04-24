package live.lingting.component.aliyun.oss.domain;

import lombok.Data;

/**
 * @author lingting 2023-04-21 14:53
 */
@Data
public class AliOssCredentials {

	private String region;

	private String bucket;

	private String objectKey;

	private String accessKeyId;

	private String accessKeySecret;

	/**
	 * 过期时间戳-毫秒
	 */
	private Long expiration;

	private String securityToken;

}
