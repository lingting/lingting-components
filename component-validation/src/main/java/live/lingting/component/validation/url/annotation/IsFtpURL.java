package live.lingting.component.validation.url.annotation;

import live.lingting.component.validation.url.validator.IsFtpUrlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * eg: ftp://192.168.2.74:3344/root/.bashrc
 *
 * @author lingting 2023-12-20 19:27
 */
@Documented
@Constraint(validatedBy = IsFtpUrlValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsFtpURL {

	String message() default "Invalid ftp URL format!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 是否允许为空
	 */
	boolean allowEmpty() default false;

}
