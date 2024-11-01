
package live.lingting.component.web.exception;

import live.lingting.component.core.enums.GlobalResultCode;
import live.lingting.component.core.exception.BizException;
import live.lingting.component.core.r.R;
import live.lingting.component.web.constant.WebConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 优先级高一点的异常处理
 *
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
@Order(WebConstants.ORDER_EXCEPTION_GLOBAL)
public class GlobalExceptionHandler {

	/**
	 * 业务异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(BizException.class)
	public R<String> handleBizException(BizException e, HttpServletRequest request) {
		log.error("请求地址: {}, 业务异常! code: {}; message: {};", request.getRequestURI(), e.getCode(), e.getMessage(),
				e.getCause());
		return R.failed(e.getCode(), e.getMessage());
	}

	/**
	 * 空指针异常捕获
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(NullPointerException.class)
	public R<String> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
		log.error("请求地址: {}, 空指针异常!", request.getRequestURI(), e);
		return R.failed(GlobalResultCode.SERVER_NLP_ERROR);
	}

	/**
	 * MethodArgumentTypeMismatchException 参数类型转换异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public R<String> handleMethodArgumentTypeMismatchException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 请求参数类型转换异常! {}", request.getRequestURI(), e.getMessage());
		return R.failed(GlobalResultCode.SERVER_PARAM_CONVERT_ERROR);
	}

	/**
	 * 请求方式有问题 - MediaType 异常 - Method 异常
	 * @return R
	 */
	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class, HttpRequestMethodNotSupportedException.class })
	public R<String> requestNotSupportedException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 请求方式异常! {}", request.getRequestURI(), e.getMessage());
		return R.failed(GlobalResultCode.SERVER_METHOD_ERROR);
	}

	/**
	 * IllegalArgumentException 异常捕获，主要用于Assert
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public R<String> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		log.error("请求地址: {}, 非法数据输入! {}", request.getRequestURI(), e.getMessage());
		return R.failed(GlobalResultCode.SERVER_PARAM_INVALID_ERROR);
	}

	/**
	 * 单体参数校验异常 validation Exception
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler({ ValidationException.class, MethodArgumentNotValidException.class, BindException.class })
	public R<String> handleValidationException(Exception e, HttpServletRequest request) {
		String message = e.getLocalizedMessage();
		FieldError fe = null;
		if (e instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException me = (MethodArgumentNotValidException) e;
			fe = me.getFieldError();
		}
		else if (e instanceof BindException) {
			fe = ((BindException) e).getFieldError();
		}

		if (fe != null) {
			message = fe.getDefaultMessage();
		}
		log.error("请求地址: {}, 参数校验异常! {}", request.getRequestURI(), e.getMessage());
		return R.failed(GlobalResultCode.PARAMS_ERROR, message);
	}

	/**
	 * 参数类型转换异常
	 */
	@ExceptionHandler(ConversionFailedException.class)
	public R<String> handlerConversionFailedException(ConversionFailedException e) {
		log.error("参数类型转换异常!", e);
		return R.failed(GlobalResultCode.PARAMS_ERROR);
	}

	/**
	 * 请求体异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public R<String> handleHttpBodyException(HttpMessageNotReadableException e, HttpServletRequest request) {
		log.error("请求地址: {}, 请求体异常! {}", request.getRequestURI(), e.getMessage());
		return R.failed(GlobalResultCode.PARAM_BODY_ERROR);
	}

	/**
	 * 参数缺失异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public R<String> handleParamsMissingException(MissingServletRequestParameterException e,
			HttpServletRequest request) {
		log.error("请求地址: {}, 参数缺失异常! {}", request.getRequestURI(), e.getMessage());
		return R.failed(GlobalResultCode.PARAM_MISSING_ERROR);
	}

	/**
	 * 鉴权异常
	 */
	@ExceptionHandler(SecurityException.class)
	public R<String> handlerSecurityException(SecurityException e) {
		return R.failed(GlobalResultCode.UNAUTHORIZED_ERROR, e.getMessage());
	}

	/**
	 * sql执行异常
	 */
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public R<String> handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
		log.error("sql执行异常!", e);
		return R.failed(GlobalResultCode.DB_CONSTRAINT_VIOLATION_ERROR);
	}

	/**
	 * 404异常. 需要配合两个设置
	 * <p>
	 * 1. spring.mvc.throw-exception-if-no-handler-found=true
	 * </p>
	 * <p>
	 * 2. spring.web.resources.add-mappings=false
	 * </p>
	 */
	@ExceptionHandler(NoHandlerFoundException.class)
	public R<String> handlerNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
		log.error("请求地址: {}, 404异常! {}", request.getRequestURI(), e.getMessage());
		return R.failed(GlobalResultCode.NOT_FOUND_ERROR);
	}

}
