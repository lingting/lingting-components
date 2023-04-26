package live.lingting.component.web.exception;

import live.lingting.component.core.R;
import live.lingting.component.core.enums.GlobalResultCode;
import live.lingting.component.web.constant.WebConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 优先级低的默认异常处理
 *
 * @author lingting 2022/9/21 15:55
 */
@Slf4j
@RestControllerAdvice
@ResponseStatus(HttpStatus.OK)
@Order(WebConstants.ORDER_EXCEPTION_DEFAULT)
public class DefaultExceptionHandler {

	/**
	 * 其他异常
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	public R<String> handleException(Exception e, HttpServletRequest request) {
		log.error("请求地址: {}, 未知异常!", request.getRequestURI(), e);
		return R.failed(GlobalResultCode.SERVER_ERROR);
	}

}
