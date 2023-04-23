package live.lingting.component.core.util;

import com.fasterxml.jackson.annotation.JsonValue;
import live.lingting.component.core.constant.StringConstants;
import live.lingting.component.core.domain.ClassField;
import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingting 2022/12/20 14:52
 */
@UtilityClass
public class EnumUtils {

	static final Map<Class<?>, ClassField> CACHE = new ConcurrentHashMap<>();

	public static ClassField getCf(Class<?> cls) {
		return CACHE.computeIfAbsent(cls, k -> {
			// 查找 IEnum
			Method method = ReflectionUtils.findMethod(k, "getValue");

			if (method != null) {
				return new ClassField(null, method);
			}

			// 查找 JsonValue 注解
			Field field = getJsonValueField(k);
			if (field != null) {
				// public 字段, 直接用
				if (Modifier.isPublic(field.getModifiers())) {
					return new ClassField(field, null);
				}

				method = ReflectionUtils.findMethod(k, StringConstants.GET + StringUtils.firstUpper(field.getName()));

				if (method != null) {
					return new ClassField(null, method);
				}
			}

			method = ReflectionUtils.findMethod(k, "name");
			return method == null ? null : new ClassField(null, method);
		});
	}

	public static <E extends Enum<E>> Object getValue(Enum<E> e) {
		if (e == null) {
			return null;
		}
		ClassField cf = getCf(e.getClass());
		try {
			return cf.invoke(e);
		}
		catch (Exception ex) {
			return null;
		}
	}

	private Field getJsonValueField(Class<?> aClass) {
		Field[] fields = aClass.getDeclaredFields();

		for (Field field : fields) {
			Annotation jsonValue = field.getAnnotation(JsonValue.class);
			if (jsonValue != null) {
				return field;
			}
		}
		return null;
	}

}
