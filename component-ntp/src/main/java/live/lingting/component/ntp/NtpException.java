package live.lingting.component.ntp;

/**
 * @author lingting 2022/11/24 10:13
 */
public class NtpException extends RuntimeException {

	public NtpException(String message) {
		super(message);
	}

	public NtpException(String message, Throwable cause) {
		super(message, cause);
	}

}
