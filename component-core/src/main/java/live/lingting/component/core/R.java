package live.lingting.component.core;

import live.lingting.component.core.enums.GlobalResultCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于接口返回数据, 除文件下载类接口, 其他接口必须使用此类做返回值
 *
 * @author lingting 2022/9/19 13:51
 */
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class R<T> implements Serializable {

	private static final long serialVersionUID = 20220919;

	private final Integer code;

	private final transient T data;

	private String message;

	public static <T> R<T> of(int code, String message) {
		return of(code, message, null);
	}

	public static <T> R<T> of(int code, String message, T data) {
		return new R<>(code, data, message);
	}

	public static <T> R<T> ok() {
		return ok(null);
	}

	public static <T> R<T> ok(T data) {
		return ok(GlobalResultCode.SUCCESS, data);
	}

	public static <T> R<T> ok(ResultCode code, T data) {
		return of(code.getCode(), data, code.getMessage());
	}

	public static <T> R<T> failed(ResultCode code) {
		return of(code.getCode(), code.getMessage());
	}

	public static <T> R<T> failed(ResultCode code, String message) {
		return of(code.getCode(), message);
	}

	public static <T> R<T> failed(Integer code, String message) {
		return of(code, null, message);
	}

	public static <T> R<T> of(Integer code, T data, String message) {
		return new R<>(code, data, message);
	}

	public boolean isOk() {
		return GlobalResultCode.SUCCESS.getCode().equals(getCode());
	}

}
