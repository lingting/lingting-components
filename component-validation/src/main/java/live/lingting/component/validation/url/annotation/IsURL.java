package live.lingting.component.validation.url.annotation;

import live.lingting.component.validation.url.validator.IsUrlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2023-12-20 19:27
 */
@Documented
@Constraint(validatedBy = IsUrlValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsURL {

	String message() default "Invalid URL format";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 是否允许为空
	 */
	boolean allowEmpty() default false;

	/**
	 * 必须不是http地址
	 */
	boolean notHttp() default false;

	/**
	 * 必须不是ftp地址
	 */
	boolean notFtp() default false;

	/**
	 * 必须不是file地址
	 */
	boolean notFile() default false;

}
