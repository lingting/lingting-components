package live.lingting.component.aliyun.oss;

import com.aliyun.sts20150401.models.AssumeRoleResponseBody;
import live.lingting.component.aliyun.oss.domain.AliOssCredentials;
import live.lingting.component.aliyun.oss.proerties.AliOssProperties;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

/**
 * @author lingting 2023-04-19 10:47
 */
@RequiredArgsConstructor
public class AliOss {

	private final AliOssProperties properties;

	private final AliSts sts;

	public AliOssCredentials credentials(String action, String objectKey) {
		AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials stsCredentials = sts.credentials(action,
				String.format("acs:oss:*:*:%s/%s", properties.getBucket(), objectKey));

		AliOssCredentials credentials = new AliOssCredentials();
		credentials.setRegion(properties.getRegion());
		credentials.setBucket(properties.getBucket());

		credentials.setAccessKeyId(stsCredentials.getAccessKeyId());
		credentials.setAccessKeySecret(stsCredentials.getAccessKeySecret());
		credentials.setSecurityToken(stsCredentials.getSecurityToken());

		// 2023-04-21T07:59:02Z
		String expiration = stsCredentials.getExpiration();
		Instant instant = Instant.parse(expiration);
		long epochMilli = instant.toEpochMilli();
		credentials.setExpiration(epochMilli);

		credentials.setObjectKey(objectKey);
		return credentials;
	}

}
