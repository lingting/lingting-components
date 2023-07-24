package live.lingting.component.security.store;

import live.lingting.component.security.mapstruct.SecurityMapstruct;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.vo.AuthorizationVO;

/**
 * @author lingting 2023-04-06 15:55
 */
public interface SecurityStore {

	void save(SecurityScope scope);

	void update(SecurityScope scope);

	void deleted(SecurityScope scope);

	SecurityScope get(String token);

	default AuthorizationVO convert(SecurityScope scope) {
		return SecurityMapstruct.INSTANCE.toVo(scope);
	}

}
