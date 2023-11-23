package live.lingting.component.grpc.server.interceptor;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import live.lingting.component.core.util.IdUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.grpc.server.properties.GrpcServerProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 在服务器端，按照拦截器注册的顺序从后到前执行，先执行后面的拦截器，再执行前面的拦截器。
 *
 * @author lingting 2023-04-13 13:23
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GrpcServerTraceIdInterceptor implements ServerInterceptor {

	private final Metadata.Key<String> traceIdKey;

	public GrpcServerTraceIdInterceptor(GrpcServerProperties properties) {
		this.traceIdKey = Metadata.Key.of(properties.getTraceIdKey(), Metadata.ASCII_STRING_MARSHALLER);
	}

	/**
	 * 从请求中获取traceId, 如果没有返回生成的traceId
	 */
	protected String traceId(Metadata headers) {
		String traceId = null;
		if (headers.containsKey(traceIdKey)) {
			traceId = headers.get(traceIdKey);
		}
		if (StringUtils.hasText(traceId)) {
			return traceId;
		}
		return IdUtils.traceId();
	}

	@Override
	public <S, R> ServerCall.Listener<S> interceptCall(ServerCall<S, R> call, Metadata headers,
			ServerCallHandler<S, R> next) {
		String traceId = traceId(headers);
		IdUtils.fillTraceId(traceId);
		try {
			// 返回traceId
			headers.put(traceIdKey, traceId);
			return next.startCall(call, headers);
		}
		finally {
			IdUtils.remoteTraceId();
		}
	}

}
