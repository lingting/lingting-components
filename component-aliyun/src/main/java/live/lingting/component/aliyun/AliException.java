package live.lingting.component.aliyun;

import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;

/**
 * @author lingting 2023-11-11 15:16
 */
public class AliException extends RuntimeException {

	public AliException(Throwable throwable) {
		super(convertMessage(throwable), throwable);
	}

	static String convertMessage(Throwable e) {
		String prefix;

		if (e instanceof ServerException) {
			String code = ((ServerException) e).getErrCode();
			String msg = ((ServerException) e).getErrMsg();
			String requestId = ((ServerException) e).getRequestId();
			prefix = String.format("code: %s; message: %s; requestId: %s;", code, msg, requestId);
		}
		else if (e instanceof ClientException) {
			String code = ((ClientException) e).getErrCode();
			String msg = ((ClientException) e).getErrMsg();
			String requestId = ((ClientException) e).getRequestId();
			prefix = String.format("code: %s; message: %s; requestId: %s;", code, msg, requestId);
		}
		else {
			prefix = "";
		}

		return String.format("%s\t%s", prefix, e.getMessage());
	}

}
