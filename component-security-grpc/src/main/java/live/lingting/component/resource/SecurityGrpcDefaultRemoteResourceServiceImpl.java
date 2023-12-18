package live.lingting.component.resource;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import live.lingting.component.convert.SecurityGrpcConvert;
import live.lingting.component.grpc.client.GrpcClientProvide;
import live.lingting.component.interceptor.SecurityGrpcRemoteResourceClientInterceptor;
import live.lingting.component.properties.SecurityGrpcProperties;
import live.lingting.component.security.properties.SecurityProperties;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.token.SecurityToken;
import live.lingting.component.security.vo.AuthorizationVO;
import live.lingting.protobuf.SecurityGrpcAuthorization;
import live.lingting.protobuf.SecurityGrpcAuthorizationServiceGrpc;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author lingting 2023-12-18 16:30
 */
public class SecurityGrpcDefaultRemoteResourceServiceImpl implements SecurityResourceService, DisposableBean {

	protected final ManagedChannel channel;

	protected final SecurityGrpcAuthorizationServiceGrpc.SecurityGrpcAuthorizationServiceBlockingStub blocking;

	protected final SecurityGrpcConvert convert;

	public SecurityGrpcDefaultRemoteResourceServiceImpl(SecurityProperties properties,
			SecurityGrpcProperties grpcProperties, GrpcClientProvide provide, SecurityGrpcConvert convert) {
		SecurityProperties.Authorization authorization = properties.getAuthorization();
		channel = provide.channel(authorization.getRemoteHost(),
				builder -> builder.intercept(new SecurityGrpcRemoteResourceClientInterceptor(grpcProperties)));
		blocking = provide.stub(channel, SecurityGrpcAuthorizationServiceGrpc::newBlockingStub);
		this.convert = convert;
	}

	@Override
	public SecurityScope resolve(SecurityToken token) {
		try {
			SecurityGrpcRemoteResourceHolder.set(token);
			SecurityGrpcAuthorization.AuthorizationVO authorizationVO = blocking.resolve(Empty.getDefaultInstance());
			AuthorizationVO vo = convert.toJava(authorizationVO);
			return convert.toScope(vo);
		}
		finally {
			SecurityGrpcRemoteResourceHolder.remove();
		}
	}

	@Override
	public void destroy() throws Exception {
		channel.shutdownNow();
	}

}
