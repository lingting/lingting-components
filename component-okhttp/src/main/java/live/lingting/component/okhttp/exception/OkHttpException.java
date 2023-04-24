package live.lingting.component.okhttp.exception;

/**
 * @author lingting
 */
public class OkHttpException extends RuntimeException {

	public OkHttpException(String message) {
		super(message);
	}

	public OkHttpException(Throwable cause) {
		super(cause);
	}

	public OkHttpException(String message, Throwable cause) {
		super(message, cause);
	}

}
