package live.lingting.component.web.converter;

import cn.hutool.core.convert.Convert;
import live.lingting.component.core.domain.ClassField;
import live.lingting.component.core.util.EnumUtils;
import org.springframework.core.convert.TypeDescriptor;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author lingting 2022/9/28 12:17
 */
public class EnumConverter implements Converter {

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		// 来源类型判断
		return (String.class.isAssignableFrom(sourceType.getType())
				|| Number.class.isAssignableFrom(sourceType.getType()))
				// 目标类型判断
				&& targetType.getType().isEnum();
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> set = new HashSet<>();
		set.add(new ConvertiblePair(String.class, Enum.class));
		set.add(new ConvertiblePair(Number.class, Enum.class));
		return set;
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}

		ClassField cf = EnumUtils.getCf(targetType.getType());

		// 来源 转化成 目标类型
		if (cf == null || (source = Convert.convert(cf.getValueType(), source)) == null) {
			return null;
		}
		for (Object o : targetType.getType().getEnumConstants()) {
			Object value = EnumUtils.getValue((Enum<?>) o);
			if (Objects.equals(source, value)) {
				return o;
			}
		}
		return null;
	}

}
