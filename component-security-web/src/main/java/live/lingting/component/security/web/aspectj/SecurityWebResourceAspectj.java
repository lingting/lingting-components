package live.lingting.component.security.web.aspectj;

import live.lingting.component.security.authorize.SecurityAuthorize;
import live.lingting.component.security.constant.SecurityConstants;
import live.lingting.component.security.annotation.Authorize;
import live.lingting.component.security.web.properties.SecurityWebProperties;
import live.lingting.component.core.util.AspectUtils;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.web.filter.WebScope;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;

import java.util.Set;

/**
 * @author lingting 2023-03-30 14:04
 */
@Aspect
@RequiredArgsConstructor
@Order(SecurityConstants.ORDER_RESOURCE_ASPECTJ)
public class SecurityWebResourceAspectj {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	private final SecurityWebProperties properties;

	private final SecurityAuthorize securityAuthorize;

	@Pointcut("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller) ) "
			+ "&& execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
	public void pointCut() {
		//
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		// 不是忽略鉴权的uri. 走鉴权流程
		if (!isIgnoreUri()) {
			Authorize authorize = AspectUtils.getAnnotation(point, Authorize.class);
			securityAuthorize.valid(authorize);
		}
		return point.proceed();
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

}
