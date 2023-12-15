package live.lingting.component.security.web.filter;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.token.SecurityToken;
import live.lingting.component.security.web.constant.SecurityWebConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lingting 2023-03-29 21:18
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityWebResourceFilter extends OncePerRequestFilter {

	private final SecurityResourceService service;

	@Override
	protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
			@NotNull FilterChain filterChain) throws ServletException, IOException {
		handlerScope(request);

		try {
			filterChain.doFilter(request, response);
		}
		finally {
			service.removeScope();
		}

	}

	protected void handlerScope(HttpServletRequest request) {
		SecurityToken token = getToken(request);
		// token有效, 设置上下文
		if (token.isAvailable()) {
			try {
				SecurityScope scope = service.resolve(token);
				service.setScope(scope);
			}
			catch (Exception e) {
				log.error("resolve token error! token: {}", token, e);
			}
		}
	}

	public SecurityToken getToken(HttpServletRequest request) {
		String raw = request.getHeader(SecurityWebConstants.TOKEN_HEADER);

		// 走参数
		if (!StringUtils.hasText(raw)) {
			return getTokenByParams(request);
		}

		return SecurityToken.ofDelimiter(raw, SecurityWebConstants.TOKEN_DELIMITER);
	}

	public SecurityToken getTokenByParams(HttpServletRequest request) {
		String value = request.getParameter(SecurityWebConstants.TOKEN_PARAMETER);
		return SecurityToken.of(SecurityWebConstants.TOKEN_TYPE_BEARER, value, value);
	}

}
