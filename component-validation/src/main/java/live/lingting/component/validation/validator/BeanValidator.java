package live.lingting.component.validation.validator;

import lombok.SneakyThrows;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.engine.valuecontext.ValueContext;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 此类的子类是真正进行校验的类.
 *
 * @author lingting 2022/11/1 20:28
 */
@SuppressWarnings({ "java:S3011", "java:S135", "java:S112", "java:S2386" })
public interface BeanValidator<A extends Annotation, V> {

	Map<Class<?>, Map<String, Field>> MAP = new ConcurrentHashMap<>();

	/**
	 * 注解校验执行
	 * @param annotation 注解
	 * @param oldValue 校验值
	 * @param validatorContext 当前校验对象上下文
	 * @param valueContext 值上下文
	 * @return boolean true 表示校验通过
	 * @throws Exception 异常
	 */
	boolean valid(A annotation, V oldValue, ConstraintValidatorContext validatorContext,
			ValueContext<Object, V> valueContext) throws Exception;

	/**
	 * 更新值上下文中的对象 当前校验字段值为新值
	 * @param annotation 注解对象
	 * @param validatorContext 校验上下文
	 * @param valueContext 值上下文
	 * @param oldVal 旧值
	 * @param newVal 新值
	 * @throws Exception 获取异常
	 */
	default void set(A annotation, ConstraintValidatorContext validatorContext, ValueContext<Object, V> valueContext,
			V oldVal, V newVal) throws Exception {
		Object bean = valueContext.getCurrentBean();
		Field field = getField(annotation, validatorContext, valueContext, oldVal);
		field.set(bean, newVal);
	}

	/**
	 * 获取字段
	 * @param annotation 注解对象
	 * @param validatorContext 校验上下文
	 * @param valueContext 值上下文
	 * @param oldVal 旧值
	 * @return java.lang.reflect.Field
	 */
	default Field getField(A annotation, ConstraintValidatorContext validatorContext,
			ValueContext<Object, V> valueContext, V oldVal) {
		PathImpl path = valueContext.getPropertyPath();
		String fieldKey = path.toString();

		Object bean = valueContext.getCurrentBean();
		Class<?> cls = bean.getClass();

		Map<String, Field> fieldMap = MAP.computeIfAbsent(cls, k -> new ConcurrentHashMap<>());

		return fieldMap.computeIfAbsent(fieldKey, new Function<String, Field>() {
			@Override
			@SneakyThrows
			public Field apply(String s) {
				Field field = getFieldByPath(path, cls);

				if (field == null) {
					field = getByValueEqual(annotation, oldVal, bean);
				}

				if (field != null) {
					field.setAccessible(true);
				}
				return field;
			}
		});
	}

	/**
	 * 从上下文中获取字段
	 * @param cls 类
	 * @return 字段
	 */
	default Field getFieldByPath(PathImpl path, Class<?> cls) throws NoSuchFieldException {
		NodeImpl node = path.getLeafNode();
		String fieldName = node.getName();
		return cls.getDeclaredField(fieldName);
	}

	/**
	 * 通过字段值比对来获取要修改的字段
	 * @param annotation 注解对象
	 * @param oldVal 旧值
	 * @param bean 旧值所属对象
	 * @return java.lang.reflect.Field
	 * @throws IllegalAccessException 异常
	 */
	default Field getByValueEqual(A annotation, V oldVal, Object bean) throws IllegalAccessException {
		Class<?> cls = bean.getClass();
		// 获取字段
		Field field = null;

		Class<? extends Annotation> aClass = annotation.getClass();
		if (!aClass.isAnnotation()) {
			aClass = annotation.annotationType();
		}

		for (Field declaredField : cls.getDeclaredFields()) {
			if (declaredField.getAnnotation(aClass) == null) {
				continue;
			}
			declaredField.setAccessible(true);
			Object fieldVal = declaredField.get(bean);

			if (Objects.equals(fieldVal, oldVal)) {
				field = declaredField;
				break;
			}
		}
		return field;
	}

}
