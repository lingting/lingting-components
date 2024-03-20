package live.lingting.component.core.util;

import lombok.experimental.UtilityClass;
import org.slf4j.MDC;

import java.util.UUID;

/**
 * @author lingting 2022/12/11 20:14
 */
@UtilityClass
public class IdUtils {

	public static final String TRACE_ID = "traceId";

	public static final String HEADER_TRACE_ID = "X-Trace-Id";

	public static String traceId() {
		return UUID.randomUUID().toString().replace("-", "");
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

	public static void removeTraceId() {
		MDC.remove(TRACE_ID);
	}

}
