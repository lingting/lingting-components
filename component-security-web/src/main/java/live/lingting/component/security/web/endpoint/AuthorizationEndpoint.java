package live.lingting.component.security.web.endpoint;

import live.lingting.component.security.annotation.Authorize;
import live.lingting.component.security.authorize.SecurityAuthorizationService;
import live.lingting.component.security.configuration.SecurityAuthorizationConfiguration;
import live.lingting.component.security.exception.AuthorizationException;
import live.lingting.component.security.password.SecurityPassword;
import live.lingting.component.security.po.AuthorizationPasswordPO;
import live.lingting.component.security.resource.SecurityHolder;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.store.SecurityStore;
import live.lingting.component.security.vo.AuthorizationVO;
import live.lingting.component.security.web.constant.SecurityWebConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lingting 2023-03-30 13:30
 */
@RestController
@RequiredArgsConstructor
@ConditionalOnBean(SecurityAuthorizationConfiguration.class)
public class AuthorizationEndpoint {

	private final SecurityAuthorizationService service;

	private final SecurityStore store;

	private final SecurityPassword securityPassword;

	@Authorize
	@DeleteMapping(SecurityWebConstants.URI_LOGOUT)
	public AuthorizationVO logout() {
		SecurityScope scope = SecurityHolder.scope();
		store.deleted(scope);
		return store.convert(scope);
	}

	@Authorize(anyone = true)
	@GetMapping(SecurityWebConstants.URI_PASSWORD)
	public AuthorizationVO password(AuthorizationPasswordPO po) {
		String username = po.getUsername();
		String rawPassword = po.getPassword();
		String password = securityPassword.decodeFront(rawPassword);
		SecurityScope scope = service.validAndBuildScope(username, password);
		if (scope == null) {
			throw new AuthorizationException("用户名或者密码错误!");
		}
		store.save(scope);
		return store.convert(scope);
	}

	@Authorize
	@GetMapping(SecurityWebConstants.URI_REFRESH)
	public AuthorizationVO refresh() {
		SecurityScope scope = service.refresh(SecurityHolder.token());
		if (scope == null) {
			throw new AuthorizationException("登录授权已失效!");
		}
		store.update(scope);
		return store.convert(scope);
	}

	@Authorize
	@GetMapping(SecurityWebConstants.URI_RESOLVE)
	public AuthorizationVO resolve() {
		SecurityScope scope = SecurityHolder.scope();
		return store.convert(scope);
	}

}
