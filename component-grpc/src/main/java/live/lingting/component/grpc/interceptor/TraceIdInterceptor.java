package live.lingting.component.grpc.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import live.lingting.component.core.util.IdUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.grpc.constant.GrpcConstants;
import org.springframework.core.annotation.Order;

/**
 * 在服务器端，按照拦截器注册的顺序从后到前执行，先执行后面的拦截器，再执行前面的拦截器。
 *
 * @author lingting 2023-04-13 13:23
 */
@Order(GrpcConstants.ORDER_TRACE_ID)
public class TraceIdInterceptor implements ServerInterceptor, ClientInterceptor {

	private static final Metadata.Key<String> KEY_TRACE_ID = Metadata.Key.of(IdUtils.TRACE_ID,
			Metadata.ASCII_STRING_MARSHALLER);

	@Override
	public <S, R> ServerCall.Listener<S> interceptCall(ServerCall<S, R> call, Metadata headers,
			ServerCallHandler<S, R> next) {
		String traceId = traceId(headers);
		IdUtils.fillTraceId(traceId);
		try {
			// 返回traceId
			headers.put(KEY_TRACE_ID, traceId);
			return next.startCall(call, headers);
		}
		finally {
			IdUtils.remoteTraceId();
		}
	}

	protected String traceId(Metadata headers) {
		String traceId = null;
		if (headers.containsKey(KEY_TRACE_ID)) {
			traceId = headers.get(KEY_TRACE_ID);
		}
		if (StringUtils.hasText(traceId)) {
			return traceId;
		}
		return IdUtils.traceId();
	}

	@Override
	public <S, R> ClientCall<S, R> interceptCall(MethodDescriptor<S, R> method, CallOptions callOptions, Channel next) {
		String traceId = IdUtils.getTraceId();

		ClientCall<S, R> call = next.newCall(method, callOptions);
		return new ForwardingClientCall.SimpleForwardingClientCall<S, R>(call) {
			@Override
			public void start(Listener<R> responseListener, Metadata headers) {
				if (StringUtils.hasText(traceId)) {
					headers.put(KEY_TRACE_ID, traceId);
				}
				super.start(responseListener, headers);
			}
		};
	}

}
