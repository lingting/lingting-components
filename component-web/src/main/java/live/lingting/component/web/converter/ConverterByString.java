package live.lingting.component.web.converter;

import live.lingting.component.core.util.StringUtils;
import org.springframework.core.convert.TypeDescriptor;

/**
 * @author lingting 2024-04-25 11:41
 */
public interface ConverterByString<T> extends Converter<T> {

	@Override
	default T value(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String str = source.toString();
		if (StringUtils.hasText(str)) {
			return value(str, sourceType, targetType);
		}
		return emptyValue(sourceType, targetType);
	}

	T emptyValue(TypeDescriptor sourceType, TypeDescriptor targetType);

	T value(String source, TypeDescriptor sourceType, TypeDescriptor targetType);

}
