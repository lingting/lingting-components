package live.lingting.component.web.converter;

import live.lingting.component.core.constant.GlobalConstants;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2022/9/28 11:14
 */
public interface Converter<T> extends ConditionalGenericConverter {

	default String[] toArray(Object source) {
		if (source == null) {
			return new String[0];
		}
		String string = ((String) source).trim();

		if (string.startsWith(GlobalConstants.SQUARE_BRACKETS_LEFT)
				&& string.endsWith(GlobalConstants.SQUARE_BRACKETS_RIGHT)) {
			string = string.substring(1, string.length() - 1);
		}
		return StringUtils.commaDelimitedListToStringArray(string);
	}

	@Override
	default T convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return nullValue(sourceType, targetType);
		}
		return value(source, sourceType, targetType);
	}

	T nullValue(TypeDescriptor sourceType, TypeDescriptor targetType);

	T value(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

}
