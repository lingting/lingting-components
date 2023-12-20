package live.lingting.component.validation.url.annotation;

import live.lingting.component.validation.url.validator.IsFileUrlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * eg: file:///root/.bashrc
 *
 * @author lingting 2023-12-20 19:27
 */
@Documented
@Constraint(validatedBy = IsFileUrlValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsFileURL {

	String message() default "Invalid file URL format!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 是否允许为空
	 */
	boolean allowEmpty() default false;

}
