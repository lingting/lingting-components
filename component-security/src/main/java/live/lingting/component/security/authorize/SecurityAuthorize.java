package live.lingting.component.security.authorize;

import live.lingting.component.core.util.ArrayUtils;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.security.annotation.Authorize;
import live.lingting.component.security.exception.AuthorizationException;
import live.lingting.component.security.exception.PermissionsException;
import live.lingting.component.security.resource.SecurityHolder;
import live.lingting.component.security.resource.SecurityScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * @author lingting 2023-03-29 20:45
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityAuthorize {

	public Authorize findAuthorize(Class<?> cls, Method method) {
		if (method != null) {
			Authorize authorize = AnnotationUtils.findAnnotation(method, Authorize.class);
			if (authorize != null) {
				return authorize;
			}
		}

		return AnnotationUtils.findAnnotation(cls, Authorize.class);
	}

	public void valid(Class<?> cls, Method method) throws AuthorizationException {
		Authorize authorize = this.findAuthorize(cls, method);
		this.valid(authorize);
	}

	/**
	 * 校验当前权限数据是否满足指定注解的要求
	 */
	public void valid(Authorize authorize) throws PermissionsException {
		log.trace("开始进行权限校验.");
		// 未配置, 要求登录
		if (authorize == null) {
			log.trace("未配置具体权限, 仅校验登录登录权限.");
			validLogin();
			return;
		}

		// 允许匿名, 直接执行
		if (authorize.anyone()) {
			log.trace("允许匿名访问, 直接放行.");
			return;
		}

		log.trace("校验登录权限.");
		// 非匿名
		validLogin();

		// 要求系统
		if (authorize.onlySystem()) {
			log.trace("校验是否系统用户.");
			valid(SecurityScope::isSystem);
		}

		// 要求普通用户
		if (authorize.onlyNormal()) {
			log.trace("校验是否普通用户.");
			valid(scope -> !scope.isSystem());
		}

		// 校验拥有配置
		validHas(authorize);

		// 校验为拥有配置
		validNot(authorize);
	}

	protected void validHas(Authorize authorize) {
		// 要求所有角色
		log.debug("校验要求所有角色.");
		valid(scope -> equals(scope.getRoles(), authorize.hasRole()));
		// 要求任一角色
		log.debug("校验要求任一角色.");
		valid(scope -> contains(scope.getRoles(), authorize.hasAnyRole()));
		// 要求所有权限
		log.debug("校验要求所有权限.");
		valid(scope -> equals(scope.getPermissions(), authorize.hasPermissions()));
		// 要求任一权限
		log.debug("校验要求任一权限.");
		valid(scope -> contains(scope.getPermissions(), authorize.hasAnyPermissions()));
	}

	protected void validNot(Authorize authorize) {
		// @formatter:off
		// 要求未拥有所有角色
		log.debug("校验要求未拥有所有角色.");
		valid(scope ->  ArrayUtils.isEmpty(authorize.notRole()) || !equals(scope.getRoles(), authorize.notRole()));
		// 要求未拥有任一角色
		log.debug("校验要求未拥有任一角色.");
		valid(scope ->  ArrayUtils.isEmpty(authorize.notAnyRole()) || !contains(scope.getRoles(), authorize.notAnyRole()));
		// 要求未拥有所有权限
		log.debug("校验要求未拥有所有权限.");
		valid(scope ->  ArrayUtils.isEmpty(authorize.notPermissions()) || !equals(scope.getPermissions(), authorize.notPermissions()));
		// 要求未拥有任一权限
		log.debug("校验要求未拥有任一权限.");
		valid(scope ->  ArrayUtils.isEmpty(authorize.notAnyPermissions()) || !contains(scope.getPermissions(), authorize.notAnyPermissions()));
		// @formatter:on
	}

	protected void validLogin() {
		valid(scope -> {
			boolean isLogin = scope != null && scope.isLogin();
			if (!isLogin) {
				throw new AuthorizationException("未获取到授权用户信息!");
			}
			if (!scope.enabled()) {
				throw new AuthorizationException("授权用户状态异常!");
			}
			return true;
		});
	}

	protected boolean contains(Collection<String> havaArray, String[] needArray) {
		// 需要为空. true
		if (ArrayUtils.isEmpty(needArray)) {
			return true;
		}
		// 拥有为空 false
		if (CollectionUtils.isEmpty(havaArray)) {
			return false;
		}

		for (String need : needArray) {
			// 任一存在则true
			if (havaArray.contains(need)) {
				return true;
			}
		}
		return false;
	}

	protected boolean equals(Collection<String> havaArray, String[] needArray) {
		// 需要为空. true
		if (ArrayUtils.isEmpty(needArray)) {
			return true;
		}
		// 拥有为空 false
		if (CollectionUtils.isEmpty(havaArray)) {
			return false;
		}

		for (String need : needArray) {
			// 任一不存在则false
			if (!havaArray.contains(need)) {
				return false;
			}
		}
		return true;
	}

	protected void valid(Predicate<SecurityScope> predicate) {
		SecurityScope scope = SecurityHolder.scope();
		boolean flag = predicate.test(scope);
		if (!flag) {
			throw new PermissionsException("未拥有访问权限");
		}
	}

}
