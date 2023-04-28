package live.lingting.component.core.util;

import live.lingting.component.core.constant.HttpConstants;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lingting 2022/10/28 17:54
 */
@UtilityClass
public class HttpServletUtils {

	public static String host(HttpServletRequest request) {
		return request.getHeader(HttpConstants.HEADER_HOST);
	}

	public static String language(HttpServletRequest request) {
		return request.getHeader(HttpConstants.HEADER_ACCEPT_LANGUAGE);
	}

	public static String authorization(HttpServletRequest request) {
		return request.getHeader(HttpConstants.HEADER_AUTHORIZATION);
	}

	public static String userAgent(HttpServletRequest request) {
		return request.getHeader(HttpConstants.HEADER_USER_AGENT);
	}

}
