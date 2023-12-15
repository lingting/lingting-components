package live.lingting.component.interceptor;

import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.exception.SecurityGrpcExceptionHandler;
import live.lingting.component.grpc.server.GrpcServer;
import live.lingting.component.properties.SecurityGrpcProperties;
import live.lingting.component.security.annotation.Authorize;
import live.lingting.component.security.authorize.SecurityAuthorize;
import live.lingting.component.security.exception.AuthorizationException;
import live.lingting.component.security.exception.PermissionsException;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.resource.SecurityScope;
import live.lingting.component.security.token.SecurityToken;
import live.lingting.component.spring.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author lingting 2023-12-14 16:28
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 100)
public class SecurityGrpcResourceInterceptor implements ServerInterceptor {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	private final SecurityGrpcProperties properties;

	private final Metadata.Key<String> authorizationKey;

	private final SecurityResourceService service;

	private final SecurityAuthorize securityAuthorize;

	private final SecurityGrpcExceptionHandler exceptionHandler;

	public SecurityGrpcResourceInterceptor(SecurityGrpcProperties properties, SecurityResourceService service,
			SecurityAuthorize securityAuthorize, SecurityGrpcExceptionHandler exceptionHandler) {
		this.properties = properties;
		this.authorizationKey = Metadata.Key.of(properties.getAuthorizationKey(), Metadata.ASCII_STRING_MARSHALLER);
		this.service = service;
		this.securityAuthorize = securityAuthorize;
		this.exceptionHandler = exceptionHandler;
	}

	@Override
	public <S, R> ServerCall.Listener<S> interceptCall(ServerCall<S, R> call, Metadata headers,
			ServerCallHandler<S, R> next) {
		handlerScope(headers);

		MethodDescriptor<S, R> descriptor = call.getMethodDescriptor();
		String methodName = descriptor.getFullMethodName();

		if (!isIgnoreMethod(methodName)) {
			Authorize authorize = getAuthorize(descriptor);
			Status status = null;
			try {
				securityAuthorize.valid(authorize);
			}
			catch (AuthorizationException e) {
				status = exceptionHandler.handlerAuthorizationException(e);
			}
			catch (PermissionsException e) {
				status = exceptionHandler.handlerPermissionsException(e);
			}
			catch (Exception e) {
				status = exceptionHandler.handlerOther(e);
			}

			// 权限校验异常! 关闭
			if (status != null) {
				call.close(status, headers);
				return new ServerCall.Listener<S>() {
				};
			}

		}

		return next.startCall(call, headers);
	}

	protected void handlerScope(Metadata metadata) {
		SecurityToken token = getToken(metadata);
		// token有效, 设置上下文
		if (token.isAvailable()) {
			try {
				SecurityScope scope = service.resolve(token);
				service.setScope(scope);
			}
			catch (Exception e) {
				log.error("resolve token error! token: {}", token, e);
			}
		}
	}

	protected SecurityToken getToken(Metadata metadata) {
		String raw = metadata.get(authorizationKey);
		if (!StringUtils.hasText(raw)) {
			return SecurityToken.EMPTY;
		}
		return SecurityToken.ofDelimiter(raw, " ");
	}

	protected Authorize getAuthorize(MethodDescriptor<?, ?> descriptor) {
		GrpcServer server = SpringUtils.getBean(GrpcServer.class);
		Method method = server.findMethod(descriptor);
		Authorize authorize = method.getAnnotation(Authorize.class);
		if (authorize != null) {
			return authorize;
		}
		return server.findClass(descriptor).getAnnotation(Authorize.class);
	}

	protected boolean isIgnoreMethod(String methodName) {
		Set<String> ignoreAntPatterns = properties.getIgnoreAntPatterns();
		if (CollectionUtils.isEmpty(ignoreAntPatterns)) {
			return false;
		}
		for (String antPattern : ignoreAntPatterns) {
			if (ANT_PATH_MATCHER.match(antPattern, methodName)) {
				return true;
			}
		}

		return false;
	}

}
