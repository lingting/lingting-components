package live.lingting.component.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

	public R(ResultCode resultCode, T data) {
		this.data = data;
		this.code = resultCode.getCode();
		this.message = resultCode.getMessage();
	}

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
		return new R<>(GlobalResultCode.SUCCESS, data);
	}

	public static <T> R<T> ok(ResultCode code, T data) {
		return new R<>(code, data);
	}

	public static <T> R<T> failed(ResultCode code) {
		return new R<>(code, null);
	}

	public static <T> R<T> failed(ResultCode code, String message) {
		return of(code.getCode(), message);
	}

	public static <T> R<T> failed(Integer code, String message) {
		return new R<>(code, null, message);
	}

	@JsonCreator
	public static <T> R<T> of(@JsonProperty("code") Integer code, @JsonProperty("message") String message,
			@JsonProperty("data") T data) {
		return new R<>(code, data, message);
	}

	public boolean isOk() {
		return GlobalResultCode.SUCCESS.getCode().equals(getCode());
	}

}
