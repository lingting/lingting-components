package live.lingting.component.grpc.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import live.lingting.component.core.util.IdUtils;
import live.lingting.component.grpc.constant.GrpcConstants;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;

/**
 * 在服务器端，按照拦截器注册的顺序从后到前执行，先执行后面的拦截器，再执行前面的拦截器。
 *
 * @author lingting 2023-04-13 13:23
 */
@Order(GrpcConstants.ORDER_TRACE_ID)
public class TraceIdInterceptor implements ServerInterceptor {

	private static final Metadata.Key<String> KEY_TRACE_ID = Metadata.Key.of(IdUtils.TRACE_ID,
			Metadata.ASCII_STRING_MARSHALLER);

	@Override
	public <S, R> ServerCall.Listener<S> interceptCall(ServerCall<S, R> call, Metadata headers,
			ServerCallHandler<S, R> next) {
		String traceId = IdUtils.traceId();
		MDC.put(IdUtils.TRACE_ID, traceId);
		try {
			// 返回traceId
			headers.put(KEY_TRACE_ID, traceId);
			return next.startCall(call, headers);
		}
		finally {
			MDC.remove(IdUtils.TRACE_ID);
		}
	}

}
