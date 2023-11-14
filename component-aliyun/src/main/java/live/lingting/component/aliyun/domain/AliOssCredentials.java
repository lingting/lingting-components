package live.lingting.component.aliyun.domain;

import lombok.Getter;
import lombok.Setter;

/**
 * @author lingting 2023-04-21 14:53
 */
@Getter
@Setter
public class AliOssCredentials extends AliStsCredentials {

	private String region;

	private String bucket;

	private String objectKey;

	public static AliOssCredentials of(AliStsCredentials credentials) {
		AliOssCredentials ossCredentials = new AliOssCredentials();
		ossCredentials.setSecurityToken(credentials.getSecurityToken());
		ossCredentials.setAccessKeyId(credentials.getAccessKeyId());
		ossCredentials.setAccessKeySecret(credentials.getAccessKeySecret());
		ossCredentials.setExpiration(credentials.getExpiration());
		return ossCredentials;
	}

}
