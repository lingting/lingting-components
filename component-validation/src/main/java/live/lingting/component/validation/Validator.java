package live.lingting.component.validation;

import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;
import org.hibernate.validator.internal.engine.path.NodeImpl;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.hibernate.validator.internal.engine.valuecontext.ValueContext;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingting 2022/11/1 20:28
 */
@SuppressWarnings({ "java:S3011", "java:S135", "java:S112", "java:S2386" })
public interface Validator<A extends Annotation, V> {

	Map<Class<?>, Field> MAP = new ConcurrentHashMap<>();

	/**
	 * 注解校验执行
	 * @param annotation 注解
	 * @param val 校验对象
	 * @param validatorContext 当前校验对象上下文
	 * @param valueContext 值上下文
	 * @return boolean
	 * @throws Exception 异常
	 */
	boolean valid(A annotation, V val, ConstraintValidatorContext validatorContext,
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
			Integer oldVal, V newVal) throws Exception {
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
	 * @throws IllegalAccessException 获取异常
	 * @throws NoSuchFieldException 获取异常
	 */
	default Field getField(A annotation, ConstraintValidatorContext validatorContext,
			ValueContext<Object, V> valueContext, Integer oldVal) throws IllegalAccessException, NoSuchFieldException {
		Object bean = valueContext.getCurrentBean();
		Class<?> cls = bean.getClass();

		if (MAP.containsKey(cls)) {
			return MAP.get(cls);
		}

		Field field = getFieldByContext(validatorContext, cls);

		if (field == null) {
			field = getByValueEqual(annotation, oldVal, bean);
		}

		if (field != null) {
			field.setAccessible(true);
			MAP.put(cls, field);
		}
		return field;
	}

	/**
	 * 从上下文中获取字段
	 * @param validatorContext 校验上下文
	 * @param cls 类
	 * @return 字段
	 * @throws NoSuchFieldException 异常
	 * @throws IllegalAccessException 异常
	 */
	default Field getFieldByContext(ConstraintValidatorContext validatorContext, Class<?> cls)
			throws NoSuchFieldException, IllegalAccessException {
		if (validatorContext instanceof ConstraintValidatorContextImpl) {
			Field basePathField = ConstraintValidatorContextImpl.class.getDeclaredField("basePath");
			basePathField.setAccessible(true);
			PathImpl path = (PathImpl) basePathField.get(validatorContext);
			NodeImpl node = path.getLeafNode();
			String fieldName = node.getName();
			return cls.getDeclaredField(fieldName);
		}
		return null;
	}

	/**
	 * 通过字段值比对来获取要修改的字段
	 * @param annotation 注解对象
	 * @param oldVal 旧值
	 * @param bean 旧值所属对象
	 * @return java.lang.reflect.Field
	 * @throws IllegalAccessException 异常
	 */
	default Field getByValueEqual(A annotation, Integer oldVal, Object bean) throws IllegalAccessException {
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
