package live.lingting.component.core.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.Setter;
import lombok.experimental.UtilityClass;

/**
 * @author lingting 2022/12/11 20:14
 */
@UtilityClass
public class IdUtils {

	@Setter
	private static Snowflake snowflake = new Snowflake(0, 0);

	public static final String TRACE_ID = "traceId";

	public static String simpleUuid() {
		return IdUtil.fastSimpleUUID();
	}

	public static Long snowflakeNext() {
		return snowflake.nextId();
	}

	public static String snowflakeNextString() {
		return snowflakeNext().toString();
	}

	public static String traceId() {
		return IdUtil.objectId();
	}

}
