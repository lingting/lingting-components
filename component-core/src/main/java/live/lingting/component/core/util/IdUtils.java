package live.lingting.component.core.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

/**
 * @author lingting 2022/12/11 20:14
 */
@UtilityClass
public class IdUtils {

	@Setter
	private static Snowflake snowflake = new Snowflake(0, 0);

	public static final String TRACE_ID = "traceId";

	public static final String HEADER_TRACE_ID = "X-Trace-Id";

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

	public static String getTraceId() {
		return MDC.get(TRACE_ID);
	}

	public static String fillTraceId() {
		String traceId = traceId();
		fillTraceId(traceId);
		return traceId;
	}

	public static void fillTraceId(String traceId) {
		MDC.put(TRACE_ID, traceId);
	}

	public static void remoteTraceId() {
		MDC.remove(TRACE_ID);
	}

}
