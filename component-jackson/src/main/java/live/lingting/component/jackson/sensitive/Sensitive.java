package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author lingting 2023-04-27 15:15
 */
@Inherited
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveDefaultSerializer.class)
public @interface Sensitive {

	SensitiveType value() default SensitiveType.DEFAULT;

	/**
	 * 当类型为 {@link SensitiveType#CUSTOMER} 时, 获取此class对应的bean进行脱敏
	 */
	Class<? extends AbstractSensitiveSerializer> cls() default AbstractSensitiveSerializer.class;

	String middle() default SensitiveUtils.MIDDLE;

	int prefixLength() default -1;

	int suffixLength() default -1;

}
