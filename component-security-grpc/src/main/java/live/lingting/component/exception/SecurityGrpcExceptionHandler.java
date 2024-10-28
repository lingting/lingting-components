package live.lingting.component.exception;

import io.grpc.Status;
import live.lingting.component.security.exception.AuthorizationException;
import live.lingting.component.security.exception.PermissionsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-12-15 17:15
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityGrpcExceptionHandler {

	/**
	 * 鉴权异常
	 */
	public Status handlerAuthorizationException(AuthorizationException e) {
		log.error("鉴权异常!", e);
		return Status.UNAUTHENTICATED.withCause(e);
	}

	/**
	 * 权限异常
	 */
	public Status handlerPermissionsException(PermissionsException e) {
		log.error("权限异常!", e);
		return Status.PERMISSION_DENIED.withCause(e);
	}

	/**
	 * 其他异常
	 */
	public Status handlerOther(Exception e) {
		log.error("执行异常!", e);
		return Status.ABORTED.withCause(e);
	}

}
