package live.lingting.component.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.grpc.client.sample.SimpleForwardingClientCall;
import live.lingting.component.security.resource.SecurityHolder;

/**
 * @author lingting 2024/10/28 19:23
 */
@SuppressWarnings("java:S110")
public class SecurityGrpcClientInterceptor implements ClientInterceptor {

	private final Metadata.Key<String> authorizationKey;

	public SecurityGrpcClientInterceptor(Metadata.Key<String> authorizationKey) {
		this.authorizationKey = authorizationKey;
	}

	@Override
	public <S, R> ClientCall<S, R> interceptCall(MethodDescriptor<S, R> method, CallOptions callOptions, Channel next) {
		String token = SecurityHolder.token();
		ClientCall<S, R> call = next.newCall(method, callOptions);
		return new SimpleForwardingClientCall<S, R>(call) {
			@Override
			public void onStartBefore(Listener<R> responseListener, Metadata headers) {
				if (!StringUtils.hasText(token)) {
					return;
				}
				headers.put(authorizationKey, token);
			}
		};
	}

}
