package live.lingting.component.aliyun;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.profile.DefaultProfile;
import live.lingting.component.aliyun.constant.AliConstants;
import live.lingting.component.aliyun.domain.AliOssCredentials;
import live.lingting.component.aliyun.domain.AliStsCredentials;
import live.lingting.component.aliyun.domain.AliStsPolicy;
import live.lingting.component.aliyun.domain.AliStsStatement;
import live.lingting.component.aliyun.proerties.AliStsProperties;
import live.lingting.component.core.exception.BizException;
import live.lingting.component.jackson.JacksonUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

import static live.lingting.component.core.enums.GlobalResultCode.ALI_ERROR;

/**
 * @author lingting 2023-04-21 14:23
 */
@Slf4j
public class AliSts {

	private final DefaultAcsClient client;

	private final AliStsProperties properties;

	@Getter
	private final String region;

	@Getter
	private final String accessKey;

	@Getter
	private final String accessSecret;

	@Getter
	private final String roleArn;

	@Getter
	private final String roleSessionName;

	public AliSts(AliStsProperties properties) {
		DefaultProfile profile = DefaultProfile.getProfile(properties.getRegion(), properties.getAccessKey(),
				properties.getAccessSecret());

		this.client = new DefaultAcsClient(profile);
		this.properties = properties;
		this.region = properties.getRegion();
		this.accessKey = properties.getAccessKey();
		this.accessSecret = properties.getAccessSecret();
		this.roleArn = properties.getRoleArn();
		this.roleSessionName = properties.getRoleSessionName();
	}

	public AliStsCredentials credentials(String action, String resource) {
		return credentials(Collections.singleton(action), Collections.singleton(resource));
	}

	public AliStsCredentials credentials(Collection<String> actions, Collection<String> resources) {
		AliStsPolicy policy = new AliStsPolicy();
		AliStsStatement statement = new AliStsStatement();
		statement.setEffect(AliConstants.EFFECT_ALLOW);
		statement.setActions(actions);
		statement.setResources(resources);
		policy.setStatements(Collections.singleton(statement));
		policy.setDurationSeconds(properties.getDurationSeconds());
		return credentials(policy);
	}

	public AliStsCredentials credentials(AliStsPolicy policy) throws BizException {
		AssumeRoleRequest request = new AssumeRoleRequest();
		request.setRoleArn(roleArn);
		request.setRoleSessionName(roleSessionName);
		request.setPolicy(JacksonUtils.toJson(policy));

		Long durationSeconds = policy.getDurationSeconds() == null ? properties.getDurationSeconds()
				: policy.getDurationSeconds();
		request.setDurationSeconds(durationSeconds);

		try {
			AssumeRoleResponse response = client.getAcsResponse(request);
			AssumeRoleResponse.Credentials responseCredentials = response.getCredentials();
			Assert.notNull(responseCredentials, "阿里云服务器返回的 credentials 为 null!");
			AliStsCredentials credentials = new AliStsCredentials();

			credentials.setSecurityToken(responseCredentials.getSecurityToken());
			credentials.setAccessKeyId(responseCredentials.getAccessKeyId());
			credentials.setAccessKeySecret(responseCredentials.getAccessKeySecret());

			// 2023-04-21T07:59:02Z
			String expiration = responseCredentials.getExpiration();
			Instant instant = Instant.parse(expiration);
			long epochMilli = instant.toEpochMilli();
			credentials.setExpiration(epochMilli);

			return credentials;
		}
		catch (Exception e) {
			log.error("获取阿里云sts权限异常!", new AliException(e));
			throw ALI_ERROR.with("获取资源权限异常!").toException();
		}
	}

	/**
	 * 生成指定 桶 指定对象的指定操作凭证
	 * @param action 操作 例如: {@link AliConstants#ACTION_OSS_GET_OBJECT }
	 * @param objectKey 指定对象
	 * @return 凭证信息
	 */
	public AliOssCredentials objectCredentials(String bucket, String action, String objectKey) {
		return objectCredentials(bucket, Collections.singleton(action), objectKey);
	}

	public AliOssCredentials objectCredentials(String bucket, Collection<String> actions, String objectKey) {
		String resource = String.format("acs:oss:*:*:%s/%s", bucket, objectKey);

		AliStsCredentials stsCredentials = credentials(actions, Collections.singleton(resource));

		AliOssCredentials ossCredentials = AliOssCredentials.of(stsCredentials);
		ossCredentials.setRegion(region);
		ossCredentials.setBucket(bucket);
		ossCredentials.setObjectKey(objectKey);
		return ossCredentials;
	}

}
