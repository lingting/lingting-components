package live.lingting.component.security.web.constant;

import live.lingting.component.core.constant.HttpConstants;
import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-04-28 12:35
 */
@UtilityClass
public class SecurityWebConstants {

	public static final String URI_LOGOUT = "authorization/logout";

	public static final String URI_PASSWORD = "authorization/password";

	public static final String URI_REFRESH = "authorization/refresh";

	public static final String URI_RESOLVE = "authorization/resolve";

	public static final String TOKEN_HEADER = HttpConstants.HEADER_AUTHORIZATION;

	public static final String TOKEN_PARAMETER = "token";

	public static final String TOKEN_TYPE_BEARER = "Bearer";

	public static final String TOKEN_DELIMITER = " ";

}
