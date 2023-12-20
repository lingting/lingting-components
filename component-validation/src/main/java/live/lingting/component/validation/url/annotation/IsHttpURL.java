package live.lingting.component.validation.url.annotation;

import live.lingting.component.validation.url.validator.IsHttpUrlValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * eg: https://www.baidu.com/roboot.txt
 *
 * @author lingting 2023-12-20 19:27
 */
@Documented
@Constraint(validatedBy = IsHttpUrlValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsHttpURL {

	String message() default "Invalid http(s) URL format!";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	/**
	 * 是否允许为空
	 */
	boolean allowEmpty() default false;

	/**
	 * 必须不是http地址, 只能是https
	 */
	boolean notHttp() default false;

}
