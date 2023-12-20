package live.lingting.component.validation.url.validator;

import live.lingting.component.core.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author lingting 2023-12-20 19:33
 */
public abstract class AbstractUrlValidator<A extends Annotation> implements ConstraintValidator<A, String> {

	public static final String TYPE_HTTP = "http";

	public static final String TYPE_HTTPS = "https";

	public static final String TYPE_FTP = "ftp";

	public static final String TYPE_FILE = "file";

	protected A annotation;

	@Override
	public void initialize(A constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if (!StringUtils.hasText(value)) {
			// 判断是否允许为空
			return allowEmpty();
		}

		try {
			URL url = new URL(value);
			String type = url.getProtocol();
			return isValid(type);
		}
		catch (MalformedURLException e) {
			return false;
		}
	}

	protected abstract boolean allowEmpty();

	protected abstract boolean isValid(String type);

}
