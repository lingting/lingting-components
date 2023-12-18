package live.lingting.component.interceptor;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import live.lingting.component.grpc.client.sample.SimpleForwardingClientCall;
import live.lingting.component.properties.SecurityGrpcProperties;
import live.lingting.component.resource.SecurityGrpcRemoteResourceHolder;
import live.lingting.component.security.token.SecurityToken;

/**
 * @author lingting 2023-12-18 16:37
 */
public class SecurityGrpcRemoteResourceClientInterceptor implements ClientInterceptor {

	private final Metadata.Key<String> authorizationKey;

	public SecurityGrpcRemoteResourceClientInterceptor(SecurityGrpcProperties properties) {
		this.authorizationKey = properties.authorizationKey();
	}

	@Override
	public <S, R> ClientCall<S, R> interceptCall(MethodDescriptor<S, R> method, CallOptions callOptions, Channel next) {
		ClientCall<S, R> call = next.newCall(method, callOptions);
		return new SimpleForwardingClientCall<S, R>(call) {
			@Override
			public void onStartBefore(Listener<R> responseListener, Metadata headers) {
				SecurityToken token = SecurityGrpcRemoteResourceHolder.get();
				if (token != null && token.isAvailable()) {
					headers.put(authorizationKey, token.getRaw());
				}
			}
		};
	}

}
