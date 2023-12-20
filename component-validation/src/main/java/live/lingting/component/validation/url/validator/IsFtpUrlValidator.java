package live.lingting.component.validation.url.validator;

import live.lingting.component.validation.url.annotation.IsFtpURL;

/**
 * @author lingting 2023-12-20 19:38
 */
public class IsFtpUrlValidator extends AbstractUrlValidator<IsFtpURL> {

	@Override
	protected boolean allowEmpty() {
		return annotation.allowEmpty();
	}

	@Override
	protected boolean isValid(String type) {
		return TYPE_FTP.equals(type);
	}

}
