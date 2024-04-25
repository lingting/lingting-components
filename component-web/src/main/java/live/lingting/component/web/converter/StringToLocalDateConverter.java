package live.lingting.component.web.converter;

import live.lingting.component.core.util.LocalDateTimeUtils;
import org.springframework.core.convert.TypeDescriptor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

/**
 * @author lingting 2022/9/28 12:17
 */
public class StringToLocalDateConverter implements ConverterByString<LocalDate> {

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return String.class.isAssignableFrom(sourceType.getType())
				&& LocalDate.class.isAssignableFrom(targetType.getType());
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, LocalDate.class));
	}

	@Override
	public LocalDate nullValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return emptyValue(sourceType, targetType);
	}

	@Override
	public LocalDate emptyValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return null;
	}

	@Override
	public LocalDate value(String source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return LocalDateTimeUtils.parseDate(source);
	}

}
