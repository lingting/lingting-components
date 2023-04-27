package live.lingting.component.jackson.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import live.lingting.component.jackson.enums.SensitiveType;
import live.lingting.component.jackson.serializer.SensitiveDefaultSerializer;

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

	SensitiveType type() default SensitiveType.DEFAULT;

	/**
	 * 当类型为 {@link SensitiveType#CUSTOMER} 时, 获取此class对应的bean进行脱敏
	 */
	Class<? extends JsonSerializer> cls() default JsonSerializer.class;

}
