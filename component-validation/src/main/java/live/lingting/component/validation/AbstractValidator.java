package live.lingting.component.validation;

import live.lingting.component.core.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.valuecontext.ValueContext;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

/**
 * @author lingting 2022/11/1 20:22
 */
@Slf4j
public abstract class AbstractValidator<A extends Annotation, V> implements EntityConstraintValidator<A, V, Object> {

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
			return SpringUtils.getBean(getBeanName(), Validator.class)
				.valid(annotation, validValue, validatorContext, valueContext);
		}
		catch (Exception e) {
			log.error("参数校验出现异常! beanName: {}, validValue: {}", annotation.annotationType(), validValue, e);
			return false;
		}
	}

	/**
	 * 获取参数校验实现bean的名称
	 * @return bean 名称
	 */
	public abstract String getBeanName();

}
