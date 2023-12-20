package live.lingting.component.validation.url.validator;

import live.lingting.component.validation.url.annotation.IsFileURL;

/**
 * @author lingting 2023-12-20 19:38
 */
public class IsFileUrlValidator extends AbstractUrlValidator<IsFileURL> {

	@Override
	protected boolean allowEmpty() {
		return annotation.allowEmpty();
	}

	@Override
	protected boolean isValid(String type) {
		return TYPE_FILE.equals(type);
	}

}
