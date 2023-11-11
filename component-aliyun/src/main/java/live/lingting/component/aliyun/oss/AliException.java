package live.lingting.component.aliyun.oss;

import com.aliyun.tea.TeaException;
import com.aliyun.tea.TeaRequest;
import com.aliyun.tea.TeaUnretryableException;

/**
 * @author lingting 2023-11-11 15:16
 */
public class AliException extends RuntimeException {

	public AliException(Throwable throwable) {
		super(convertMessage(throwable), throwable);
	}

	static String convertMessage(Throwable e) {
		if (e instanceof TeaUnretryableException) {
			TeaRequest request = ((TeaUnretryableException) e).getLastRequest();
			if (request != null) {
				return "path: " + request.pathname;
			}
		}

		if (e instanceof TeaException) {
			String code = ((TeaException) e).getCode();
			String message = e.getMessage();
			return String.format("code: %s; message: %s", code, message);
		}

		return "";
	}

}
