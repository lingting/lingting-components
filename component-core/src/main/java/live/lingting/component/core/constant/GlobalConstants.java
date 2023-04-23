package live.lingting.component.core.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2022/9/26 11:35
 */
@UtilityClass
public class GlobalConstants {

	public static final String COMMA = ",";

	public static final String NAME_ENV = "spring.profiles.active";

	public static final String NAME_ENV_DELIMITER = COMMA;

	/**
	 * 未被逻辑删除的标识，即有效数据标识 逻辑删除标识，普通情况下可以使用 1 标识删除，0 标识存活 但在有唯一索引的情况下，会导致索引冲突，所以用 0 标识存活，
	 * 已删除数据记录为删除时间戳
	 */
	public static final Long NOT_DELETED_FLAG = 0L;

	public static final String POINT = ".";

	public static final String SLASH = "/";

	public static final String URI_END = SLASH;

	public static final String COLON = ":";

	public static final String STRING_EMPTY = "";

	public static final String SQUARE_BRACKETS_LEFT = "[";

	public static final String SQUARE_BRACKETS_RIGHT = "]";

	public static final String BIG_BRACKETS_LEFT = "{";

	public static final String BIG_BRACKETS_RIGHT = "}";

	public static final String NULL_COLLECTION_STRING = "[]";

	public static final Integer USER_ID_UNKNOWN = -1;

	public static final Integer TENANT_ID_UNKNOWN = -1;

	/**
	 * 公司外网ip
	 */
	public static final String DEV_IP = "222.71.197.250;";

	public static final String SYSTEM_ERROR = "System Error";

}
