package live.lingting.component.aliyun.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2023-04-21 14:53
 */
@Getter
@Setter
public class AliStsCredentials {

	private String securityToken;

	private String accessKeyId;

	private String accessKeySecret;

	/**
	 * 过期时间戳-毫秒
	 */
	private Long expiration;

}
