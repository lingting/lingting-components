package live.lingting.component.resource;

import io.grpc.ManagedChannel;
import live.lingting.component.convert.SecurityGrpcConvert;
import live.lingting.component.grpc.client.GrpcClientProvide;
import live.lingting.component.security.properties.SecurityProperties;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.token.SecurityToken;
import live.lingting.component.security.vo.AuthorizationVO;
import live.lingting.protobuf.SecurityGrpcAuthorization;
import live.lingting.protobuf.SecurityGrpcAuthorizationServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author lingting 2023-12-18 16:30
 */
@Slf4j
public class SecurityGrpcDefaultRemoteResourceServiceImpl implements SecurityResourceService, DisposableBean {

	protected final ManagedChannel channel;

	protected final SecurityGrpcAuthorizationServiceGrpc.SecurityGrpcAuthorizationServiceBlockingStub blocking;

	protected final SecurityGrpcConvert convert;

	public SecurityGrpcDefaultRemoteResourceServiceImpl(SecurityProperties properties, GrpcClientProvide provide,
			SecurityGrpcConvert convert) {
		SecurityProperties.Authorization authorization = properties.getAuthorization();
		channel = provide.channel(authorization.getRemoteHost());
		blocking = provide.stub(channel, SecurityGrpcAuthorizationServiceGrpc::newBlockingStub);
		this.convert = convert;
	}

	@Override
	public SecurityScope resolve(SecurityToken token) {
		log.trace("从远端解析token: {}", token);
		SecurityGrpcAuthorization.TokenPO po = SecurityGrpcAuthorization.TokenPO.newBuilder()
			.setType(token.getType())
			.setValue(token.getToken())
			.build();
		SecurityGrpcAuthorization.AuthorizationVO authorizationVO = blocking.resolve(po);
		log.trace("从远端获取到授权: {}", authorizationVO);
		AuthorizationVO vo = convert.toJava(authorizationVO);
		return convert.toScope(vo);
	}

	@Override
	public void destroy() throws Exception {
		channel.shutdownNow();
	}

}
