package live.lingting.component.grpc.client.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import live.lingting.component.core.util.IdUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.grpc.client.properties.GrpcClientProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 在服务器端，按照拦截器注册的顺序从后到前执行，先执行后面的拦截器，再执行前面的拦截器。
 *
 * @author lingting 2023-04-13 13:23
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GrpcClientTraceIdInterceptor implements ClientInterceptor {

	private final Metadata.Key<String> traceIdKey;

	public GrpcClientTraceIdInterceptor(GrpcClientProperties properties) {
		this.traceIdKey = Metadata.Key.of(properties.getTraceIdKey(), Metadata.ASCII_STRING_MARSHALLER);
	}

	/**
	 * 获取当前上下文的traceId
	 */
	protected String traceId() {
		return IdUtils.getTraceId();
	}

	@Override
	public <S, R> ClientCall<S, R> interceptCall(MethodDescriptor<S, R> method, CallOptions callOptions, Channel next) {
		String traceId = traceId();

		ClientCall<S, R> call = next.newCall(method, callOptions);
		return new ForwardingClientCall.SimpleForwardingClientCall<S, R>(call) {
			@Override
			public void start(Listener<R> responseListener, Metadata headers) {
				if (StringUtils.hasText(traceId)) {
					headers.put(traceIdKey, traceId);
				}
				super.start(responseListener, headers);
			}
		};
	}

}
