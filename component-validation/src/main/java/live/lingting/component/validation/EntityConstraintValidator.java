package live.lingting.component.validation;

import org.hibernate.validator.internal.engine.valuecontext.ValueContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

public interface EntityConstraintValidator<A extends Annotation, V, T> extends ConstraintValidator<A, V> {

	@Override
	default boolean isValid(V value, ConstraintValidatorContext context) {
		throw new UnsupportedOperationException();
	}

	boolean isValid(V validValue, ConstraintValidatorContext validatorContext, ValueContext<T, V> valueContext);

}
