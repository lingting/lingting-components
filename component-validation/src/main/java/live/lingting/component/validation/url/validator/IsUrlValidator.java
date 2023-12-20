package live.lingting.component.validation.url.validator;

import live.lingting.component.validation.url.annotation.IsURL;

/**
 * @author lingting 2023-12-20 19:38
 */
public class IsUrlValidator extends AbstractUrlValidator<IsURL> {

	@Override
	protected boolean allowEmpty() {
		return annotation.allowEmpty();
	}

	@Override
	protected boolean isValid(String type) {
		// 不允许为http
		if (annotation.notHttp() && (TYPE_HTTP.equals(type) || TYPE_HTTPS.equals(type))) {
			return false;
		}
		// 不允许为ftp
		if (annotation.notFtp() && TYPE_FTP.equals(type)) {
			return false;
		}
		// 不允许为file
		if (annotation.notFile() && TYPE_FILE.equals(type)) {
			return false;
		}
		return TYPE_HTTP.equals(type) || TYPE_HTTPS.equals(type) || TYPE_FTP.equals(type) || TYPE_FILE.equals(type);
	}

}
