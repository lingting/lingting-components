package live.lingting.component.web.filter;

import live.lingting.component.core.util.IdUtils;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import static live.lingting.component.web.filter.TraceIdFilter.MAP_THREAD_LOCAL;

/**
 * @author lingting 2022/11/30 21:32
 */
@UtilityClass
public class WebScope {

	public static final String KEY_SCHEME = "lingting_component_web_scheme";

	public static final String KEY_HOST = "lingting_component_web_host";

	public static final String KEY_ORIGIN = "lingting_component_web_origin";

	public static final String KEY_IP = "lingting_component_web_ip";

	public static final String KEY_URI = "lingting_component_web_uri";

	public static final String KEY_LANGUAGE = "lingting_component_web_language";

	public static final String KEY_AUTHORIZATION = "lingting_component_web_Authorization";

	public static final String KEY_USER_AGENT = "lingting_component_web_user_agent";

	// region 上下文传递数据
	public static <T> void set(String key, T val) {
		MAP_THREAD_LOCAL.get().put(key, val);
	}

	@SuppressWarnings("unchecked")
	public static <T> T get(String key) {
		return (T) MAP_THREAD_LOCAL.get().get(key);
	}

	// endregion

	// region 基础数据

	public static String scheme() {
		return get(KEY_SCHEME);
	}

	public static String host() {
		return get(KEY_HOST);
	}

	public static String origin() {
		return get(KEY_ORIGIN);
	}

	public static String ip() {
		return get(KEY_IP);
	}

	public static String uri() {
		return get(KEY_URI);
	}

	public static String traceId() {
		return MDC.get(IdUtils.TRACE_ID);
	}

	// endregion

	// region 请求头数据

	public static String language() {
		return get(KEY_LANGUAGE);
	}

	public static String authorization() {
		return get(KEY_AUTHORIZATION);
	}

	public static String userAgent() {
		return get(KEY_USER_AGENT);
	}

	// endregion

}
