package live.lingting.component.security.web.resource;

import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.security.authorize.SecurityAuthorize;
import live.lingting.component.security.constant.SecurityConstants;
import live.lingting.component.security.web.properties.SecurityWebProperties;
import live.lingting.component.web.filter.WebScope;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author lingting 2024-06-11 15:07
 */
@RequiredArgsConstructor
public class SecurityWebResourceInterceptor implements HandlerInterceptor, Ordered {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	private final SecurityWebProperties properties;

	private final SecurityAuthorize securityAuthorize;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 不是忽略鉴权的uri. 走鉴权流程
		if (handler instanceof HandlerMethod && !isIgnoreUri()) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Class<?> cls = handlerMethod.getBeanType();
			Method method = handlerMethod.getMethod();
			securityAuthorize.valid(cls, method);
		}
		return true;
	}

	protected boolean isIgnoreUri() {
		Set<String> ignoreAntPatterns = properties.getIgnoreAntPatterns();
		if (CollectionUtils.isEmpty(ignoreAntPatterns)) {
			return false;
		}
		String uri = WebScope.uri();

		for (String antPattern : ignoreAntPatterns) {
			if (ANT_PATH_MATCHER.match(antPattern, uri)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public int getOrder() {
		return SecurityConstants.ORDER_RESOURCE_ASPECTJ;
	}

}
