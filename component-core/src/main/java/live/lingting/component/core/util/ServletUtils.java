package live.lingting.component.core.util;

import lombok.experimental.UtilityClass;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author lingting 2022/10/28 17:54
 */
@UtilityClass
public class ServletUtils {

	public static String getUri(ServletRequest servletRequest) {
		if (servletRequest instanceof HttpServletRequest) {
			return ((HttpServletRequest) servletRequest).getRequestURI();
		}
		return null;
	}

}
