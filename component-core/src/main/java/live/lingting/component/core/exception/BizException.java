package live.lingting.component.core.exception;

import live.lingting.component.core.r.ResultCode;
import lombok.Getter;

/**
 * @author lingting 2022/9/22 12:11
 */
public class BizException extends RuntimeException {

	@Getter
	private final Integer code;

	@Getter
	private final String message;

	public BizException(ResultCode resultCode, Object... args) {
		this(resultCode.getCode(), String.format(resultCode.getMessage(), args), null);
	}

	public BizException(ResultCode resultCode, Exception e) {
		this(resultCode, resultCode.getMessage(), e);
	}

	public BizException(ResultCode resultCode, String message, Exception e) {
		this(resultCode.getCode(), message, e);
	}

	public BizException(Integer code, String message) {
		this(code, message, null);
	}

	public BizException(Integer code, String message, Exception e) {
		super(message, e);
		this.message = message;
		this.code = code;
	}

}
