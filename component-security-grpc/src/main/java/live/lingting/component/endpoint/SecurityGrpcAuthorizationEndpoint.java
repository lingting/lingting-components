package live.lingting.component.endpoint;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import live.lingting.component.convert.SecurityGrpcConvert;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.security.annotation.Authorize;
import live.lingting.component.security.authorize.SecurityAuthorizationService;
import live.lingting.component.security.exception.AuthorizationException;
import live.lingting.component.security.password.SecurityPassword;
import live.lingting.component.security.resource.SecurityHolder;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.store.SecurityStore;
import live.lingting.component.security.vo.AuthorizationVO;
import live.lingting.protobuf.SecurityGrpcAuthorization;
import live.lingting.protobuf.SecurityGrpcAuthorizationServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-12-18 15:31
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityGrpcAuthorizationEndpoint
		extends SecurityGrpcAuthorizationServiceGrpc.SecurityGrpcAuthorizationServiceImplBase {

	private final SecurityAuthorizationService service;

	private final SecurityStore store;

	private final SecurityPassword securityPassword;

	private final SecurityGrpcConvert convert;

	@Override
	public void logout(Empty request, StreamObserver<SecurityGrpcAuthorization.AuthorizationVO> observer) {
		SecurityScope scope = SecurityHolder.scope();
		log.trace("退出登录. {}", scope);
		store.deleted(scope);
		onNext(scope, observer);
	}

	@Override
	@Authorize(anyone = true)
	public void password(SecurityGrpcAuthorization.AuthorizationPasswordPO po,
			StreamObserver<SecurityGrpcAuthorization.AuthorizationVO> observer) {
		String username = po.getUsername();
		String rawPassword = po.getPassword();
		String password = securityPassword.decodeFront(rawPassword);
		log.trace("使用密码登录获取授权. {}", username);
		SecurityScope scope = service.validAndBuildScope(username, password);
		if (scope == null) {
			throw new AuthorizationException("用户名或者密码错误!");
		}
		store.save(scope);
		onNext(scope, observer);
	}

	@Override
	public void refresh(SecurityGrpcAuthorization.TokenPO request,
			StreamObserver<SecurityGrpcAuthorization.AuthorizationVO> observer) {
		String value = request.getValue();
		if (!StringUtils.hasText(value)) {
			value = SecurityHolder.token();
		}
		log.trace("刷新授权. {}", value);
		SecurityScope scope = service.refresh(value);
		if (scope == null) {
			throw new AuthorizationException("登录授权已失效!");
		}
		store.update(scope);
		onNext(scope, observer);
	}

	@Override
	public void resolve(SecurityGrpcAuthorization.TokenPO request,
			StreamObserver<SecurityGrpcAuthorization.AuthorizationVO> observer) {
		String value = request.getValue();
		if (!StringUtils.hasText(value)) {
			value = SecurityHolder.token();
		}
		log.trace("解析授权. {}", value);
		SecurityScope scope = store.get(value);
		onNext(scope, observer);
	}

	protected void onNext(SecurityScope scope, StreamObserver<SecurityGrpcAuthorization.AuthorizationVO> observer) {
		log.trace("内部授权上下文转换可返回的授权信息. {}", scope);
		AuthorizationVO vo = store.convert(scope);
		onNext(vo, observer);
	}

	protected void onNext(AuthorizationVO vo, StreamObserver<SecurityGrpcAuthorization.AuthorizationVO> observer) {
		SecurityGrpcAuthorization.AuthorizationVO authorizationVO = convert.toProtobuf(vo);
		observer.onNext(authorizationVO);
		observer.onCompleted();
	}

}
