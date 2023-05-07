package live.lingting.component.money.web;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.money.Money;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author lingting 2023-05-07 18:38
 */
public class MoneyConverter implements ConditionalGenericConverter {

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return Object.class.isAssignableFrom(sourceType.getType())
				&& Money.class.isAssignableFrom(targetType.getType());
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object.class, Money.class));
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null || !StringUtils.hasText(source.toString())) {
			return null;
		}
		return Money.of(source.toString());
	}

}
