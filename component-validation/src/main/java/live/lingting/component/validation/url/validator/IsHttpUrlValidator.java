package live.lingting.component.validation.url.validator;

import live.lingting.component.validation.url.annotation.IsHttpURL;

/**
 * @author lingting 2023-12-20 19:38
 */
public class IsHttpUrlValidator extends AbstractUrlValidator<IsHttpURL> {

	@Override
	protected boolean allowEmpty() {
		return annotation.allowEmpty();
	}

	@Override
	protected boolean isValid(String type) {
		// 不允许为http
		if (annotation.notHttp() && TYPE_HTTP.equals(type)) {
			return false;
		}

		return TYPE_HTTP.equals(type) || TYPE_HTTPS.equals(type);
	}

}
