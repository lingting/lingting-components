package live.lingting.component.web.converter;

import live.lingting.component.core.util.LocalDateTimeUtils;
import org.springframework.core.convert.TypeDescriptor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

/**
 * @author lingting 2022/9/28 12:17
 */
public class StringToLocalDateTimeConverter implements ConverterByString<LocalDateTime> {

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return String.class.isAssignableFrom(sourceType.getType())
				&& LocalDateTime.class.isAssignableFrom(targetType.getType());
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, LocalDateTime.class));
	}

	@Override
	public LocalDateTime nullValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return emptyValue(sourceType, targetType);
	}

	@Override
	public LocalDateTime emptyValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return null;
	}

	@Override
	public LocalDateTime value(String source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return LocalDateTimeUtils.parse(source);
	}

}
