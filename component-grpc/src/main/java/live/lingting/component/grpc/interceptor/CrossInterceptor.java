package live.lingting.component.grpc.interceptor;

import live.lingting.component.grpc.constant.GrpcConstants;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

/**
 * @author lingting 2023-04-17 10:21
 */
@Slf4j
@Order(GrpcConstants.ORDER_CROSS)
public class CrossInterceptor implements ServerInterceptor {

	private static final Metadata.Key<String> KEY_ORIGIN = Metadata.Key.of("Origin", Metadata.ASCII_STRING_MARSHALLER);

	@Override
	public <R, S> ServerCall.Listener<R> interceptCall(ServerCall<R, S> call, Metadata headers,
			ServerCallHandler<R, S> next) {
		String origin = headers.get(KEY_ORIGIN);
		log.debug("收到来源: {} 的跨域请求.", origin);
		// 处理跨域请求
		return Contexts.interceptCall(Context.current(), call, headers, next);
	}

}
