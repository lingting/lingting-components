package live.lingting.component.aliyun.oss;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.profile.DefaultProfile;
import live.lingting.component.aliyun.oss.constant.AliConstants;
import live.lingting.component.aliyun.oss.domain.AliPolicy;
import live.lingting.component.aliyun.oss.domain.AliStatement;
import live.lingting.component.aliyun.oss.proerties.AliStsProperties;
import live.lingting.component.core.enums.GlobalResultCode;
import live.lingting.component.core.exception.BizException;
import live.lingting.component.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.Set;

/**
 * @author lingting 2023-04-21 14:23
 */
@Slf4j
public class AliSts {

	private final AliStsProperties properties;

	private final DefaultProfile profile;

	private final DefaultAcsClient client;

	public AliSts(AliStsProperties properties) {
		this(properties, DefaultProfile.getProfile(properties.getRegion(), properties.getAccessKey(),
				properties.getAccessSecret()));
	}

	public AliSts(AliStsProperties properties, DefaultProfile profile) {
		this.properties = properties;
		this.profile = profile;
		this.client = new DefaultAcsClient(profile);
	}

	public AssumeRoleResponse.Credentials credentials(String action, String resource) {
		return credentials(Collections.singleton(action), Collections.singleton(resource));
	}

	public AssumeRoleResponse.Credentials credentials(Set<String> actions, Set<String> resources) {
		AliPolicy policy = new AliPolicy();
		AliStatement statement = new AliStatement();
		statement.setEffect(AliConstants.EFFECT_ALLOW);
		statement.setActions(actions);
		statement.setResources(resources);
		policy.setStatements(Collections.singleton(statement));
		return credentials(policy);
	}

	public AssumeRoleResponse.Credentials credentials(AliPolicy policy) {
		AssumeRoleRequest request = new AssumeRoleRequest();
		request.setDurationSeconds(properties.getDurationSeconds());
		request.setRoleSessionName(properties.getRoleSessionName());
		request.setRoleArn(properties.getRoleArn());
		request.setPolicy(JacksonUtils.toJson(policy));

		try {
			AssumeRoleResponse response = client.getAcsResponse(request);
			AssumeRoleResponse.Credentials credentials = response.getCredentials();
			Assert.notNull(credentials, "返回credentials 为 null");
			return credentials;
		}
		catch (Exception e) {
			log.error("获取阿里云sts权限异常!", new AliException(e));
			throw new BizException(GlobalResultCode.ALI_ERROR, "获取资源权限异常!");
		}
	}

}
