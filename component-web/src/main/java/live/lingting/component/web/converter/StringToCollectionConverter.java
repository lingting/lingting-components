package live.lingting.component.web.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.core.CollectionFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * copy by StringToCollectionConverter
 *
 * @author lingting 2022/9/28 10:33
 */
@RequiredArgsConstructor
public class StringToCollectionConverter implements Converter {

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

	@Override
	@Nullable
	public Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		String[] fields = toArray(source);

		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		Collection<Object> target = CollectionFactory.createCollection(targetType.getType(),
				(elementDesc != null ? elementDesc.getType() : null), fields.length);

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
