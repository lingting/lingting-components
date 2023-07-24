package live.lingting.component.security.resource;

import live.lingting.component.core.util.StringUtils;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author lingting 2023-03-29 20:25
 */
@Data
public class SecurityScope {

	private String token;

	private String userId;

	private String tenantId;

	private String username;

	private String password;

	private String avatar;

	private String nickname;

	private Boolean isSystem;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	/**
	 * 过期时间的时间戳
	 */
	private Long expireTime;

	private Set<String> roles;

	private Set<String> permissions;

	private Map<String, Object> attributes;

	public boolean isSystem() {
		return Boolean.TRUE.equals(getIsSystem());
	}

	public boolean enabled() {
		return Boolean.TRUE.equals(getIsEnabled());
	}

	/**
	 * 此scope是否为已登录用户
	 */
	public boolean isLogin() {
		return StringUtils.hasText(getToken()) && StringUtils.hasText(getUserId()) && getIsSystem() != null
				&& getIsEnabled() != null;
	}

}
