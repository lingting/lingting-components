package live.lingting.component.okhttp.exception;

/**
 * @author lingting 2023-12-20 17:02
 */
public class OkHttpDownloadException extends RuntimeException {

	public OkHttpDownloadException(String message) {
		super(message);
	}

	public OkHttpDownloadException(String message, Throwable cause) {
		super(message, cause);
	}

}
