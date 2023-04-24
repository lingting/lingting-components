package live.lingting.component.aliyun.oss;

import com.aliyun.sts20150401.Client;
import com.aliyun.sts20150401.models.AssumeRoleRequest;
import com.aliyun.sts20150401.models.AssumeRoleResponse;
import com.aliyun.sts20150401.models.AssumeRoleResponseBody;
import com.aliyun.teaopenapi.models.Config;
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

	private final Client client;

	public AliSts(AliStsProperties properties) throws Exception {
		this.properties = properties;
		Config config = new Config().setAccessKeyId(properties.getAccessKey())
			.setAccessKeySecret(properties.getAccessSecret())
			.setEndpoint(String.format("sts.%s.aliyuncs.com", properties.getRegion()))
			.setProtocol(properties.getProtocol());
		this.client = new Client(config);
	}

	public AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials credentials(String action, String resource) {
		return credentials(Collections.singleton(action), Collections.singleton(resource));
	}

	public AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials credentials(Set<String> actions,
			Set<String> resources) {
		AliPolicy policy = new AliPolicy();
		AliStatement statement = new AliStatement();
		statement.setEffect(AliConstants.EFFECT_ALLOW);
		statement.setActions(actions);
		statement.setResources(resources);
		policy.setStatements(Collections.singleton(statement));
		return credentials(policy);
	}

	public AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials credentials(AliPolicy policy) {
		AssumeRoleRequest request = new AssumeRoleRequest().setDurationSeconds(properties.getDurationSeconds())
			.setRoleSessionName(properties.getRoleSessionName())
			.setRoleArn(properties.getRoleArn())
			.setPolicy(JacksonUtils.toJson(policy));

		try {
			AssumeRoleResponse response = client.assumeRole(request);
			AssumeRoleResponseBody body = response.getBody();
			AssumeRoleResponseBody.AssumeRoleResponseBodyCredentials credentials = body.getCredentials();
			Assert.notNull(credentials, "credentials 为null");
			return credentials;
		}
		catch (Exception e) {
			log.error("获取阿里云sts权限异常!", e);
			throw new BizException(GlobalResultCode.ALI_ERROR, "获取资源权限异常!");
		}
	}

}
