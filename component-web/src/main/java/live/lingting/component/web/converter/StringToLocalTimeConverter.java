package live.lingting.component.web.converter;

import live.lingting.component.core.util.LocalDateTimeUtils;
import org.springframework.core.convert.TypeDescriptor;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Set;

/**
 * @author lingting 2022/9/28 12:17
 */
public class StringToLocalTimeConverter implements ConverterByString<LocalTime> {

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return String.class.isAssignableFrom(sourceType.getType())
				&& LocalTime.class.isAssignableFrom(targetType.getType());
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, LocalTime.class));
	}

	@Override
	public LocalTime nullValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return emptyValue(sourceType, targetType);
	}

	@Override
	public LocalTime emptyValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return null;
	}

	@Override
	public LocalTime value(String source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		return LocalDateTimeUtils.parseTime(source);
	}

}
