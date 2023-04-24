package live.lingting.component.validation.db;

import live.lingting.component.validation.AbstractValidator;
import live.lingting.component.validation.constant.ValidatorConstants;

import java.io.Serializable;

/**
 * @author lingting 2022/11/3 9:26
 */
public class ValidDbValidator extends AbstractValidator<ValidDb, Serializable> {

	@Override
	public String getBeanName() {
		return ValidatorConstants.VALID_DB_BEAN;
	}

}
