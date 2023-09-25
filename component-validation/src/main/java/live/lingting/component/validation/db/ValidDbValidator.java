package live.lingting.component.validation.db;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.spring.util.SpringUtils;
import live.lingting.component.validation.validator.AbstractAnnotationValidator;
import live.lingting.component.validation.validator.BeanValidator;
import live.lingting.component.validation.constant.ValidatorConstants;

import java.io.Serializable;

/**
 * @author lingting 2022/11/3 9:26
 */
public class ValidDbValidator extends AbstractAnnotationValidator<ValidDb, Serializable> {

	@Override
	@SuppressWarnings("unchecked")
	public BeanValidator<ValidDb, Serializable> getValidator(ValidDb annotation) {
		Object bean;
		if (StringUtils.hasText(annotation.beanName())) {
			bean = SpringUtils.getBean(annotation.beanName());
		}
		else if (annotation.beanClas() != null) {
			bean = SpringUtils.getBean(annotation.beanClas());
		}
		else {
			bean = SpringUtils.getBean(ValidatorConstants.VALID_DB_BEAN);
		}
		return (BeanValidator<ValidDb, Serializable>) bean;
	}

}
