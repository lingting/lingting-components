package live.lingting.component.web.filter;

import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.IdUtils;
import live.lingting.component.core.util.IpUtils;
import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.web.RepeatBodyRequestWrapper;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2022/11/30 21:27
 */
public class TraceIdFilter extends OncePerRequestFilter {

	static final ThreadLocal<Map<String, Object>> MAP_THREAD_LOCAL = new ThreadLocal<>();

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			FilterChain filterChain) throws ServletException, IOException {
		final RepeatBodyRequestWrapper request = RepeatBodyRequestWrapper.of(httpServletRequest);
		final ContentCachingResponseWrapper response = new ContentCachingResponseWrapper(httpServletResponse);
		String traceId = IdUtils.traceId();

		// trace id 初始化
		MDC.put(IdUtils.TRACE_ID, traceId);
		Map<String, Object> map = new HashMap<>();
		MAP_THREAD_LOCAL.set(map);
		try {
			map.put(WebScope.KEY_HOST, "Host");
			map.put(WebScope.KEY_IP, IpUtils.getFirstIp(request));
			map.put(WebScope.KEY_LANGUAGE, request.getHeader("Accept-Language"));
			map.put(WebScope.KEY_AUTHORIZATION, request.getHeader("Authorization"));
			map.put(WebScope.KEY_USER_AGENT, request.getHeader("User-Agent"));
			response.addHeader(IdUtils.TRACE_ID, traceId);
			filterChain.doFilter(request, response);
		}
		finally {
			// 主动关闭所有已经打开过的流
			for (Closeable closeable : request.getCloseableList()) {
				StreamUtils.close(closeable);
			}
			// 移除文件, 避免大量文件放在临时目录
			FileUtils.delete(request.getBodyFile());

			MDC.remove(IdUtils.TRACE_ID);
			MAP_THREAD_LOCAL.remove();

			response.copyBodyToResponse();
		}
	}

}
