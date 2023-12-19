package live.lingting.component.security.vo;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * @author lingting 2023-03-30 13:54
 */
@Data
public class AuthorizationVO {

	private String token;

	private String tenantId;

	private String userId;

	private String username;

	private String avatar;

	private String nickname;

	private Boolean isSystem;

	/**
	 * 是否启用
	 */
	private Boolean isEnabled;

	private Set<String> roles;

	private Set<String> permissions;

	private Map<String, Object> attributes;

}
