package live.lingting.component.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * copy by StringToCollectionConverter
 *
 * @author lingting 2022/9/28 10:33
 */
@RequiredArgsConstructor
public class StringToCollectionConverter implements ConverterByString<Collection<Object>> {

	private final ConversionService conversionService;

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(String.class, Collection.class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		TypeDescriptor targetDescriptor = targetType.getElementTypeDescriptor();
		if (targetDescriptor == null) {
			return true;
		}

		return conversionService.canConvert(sourceType, targetDescriptor);
	}

	Collection<Object> collection(TypeDescriptor targetType, int length) {
		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		return CollectionFactory.createCollection(targetType.getType(),
				(elementDesc != null ? elementDesc.getType() : null), length);
	}

	@Override
	public Collection<Object> nullValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return emptyValue(sourceType, targetType);
	}

	@Override
	public Collection<Object> emptyValue(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return collection(targetType, 0);
	}

	@Override
	public Collection<Object> value(String source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String[] fields = toArray(source);

		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		Collection<Object> target = collection(targetType, fields.length);

		if (elementDesc == null) {
			for (String field : fields) {
				target.add(field.trim());
			}
		}
		else {
			for (String field : fields) {
				Object targetElement = this.conversionService.convert(field.trim(), sourceType, elementDesc);
				target.add(targetElement);
			}
		}
		return target;
	}

}
