package live.lingting.component.validation.validator;

import live.lingting.component.validation.EntityConstraintValidator;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.valuecontext.ValueContext;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * 此类的子类用于注解上使用.
 *
 * @author lingting 2022/11/1 20:22
 */
@Slf4j
public abstract class AbstractAnnotationValidator<A extends Annotation, V>
		implements EntityConstraintValidator<A, V, Object> {

	private A annotation;

	@Override
	public void initialize(A constraintAnnotation) {
		this.annotation = constraintAnnotation;
	}

	@Override
	public boolean isValid(V validValue, ConstraintValidatorContext validatorContext,
			ValueContext<Object, V> valueContext) {
		try {
			// 使用spring 解耦
			return getValidator(annotation).valid(annotation, validValue, validatorContext, valueContext);
		}
		catch (Exception e) {
			log.error("参数校验出现异常! 注解类型: {}, 校验值: {}", annotation.annotationType(), validValue, e);
			return false;
		}
	}

	public abstract BeanValidator<A, V> getValidator(A annotation);

}
