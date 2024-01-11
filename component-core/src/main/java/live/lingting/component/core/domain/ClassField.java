package live.lingting.component.core.domain;

import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 用于获取指定字段的值
 * <p>
 * 优先取指定字段的 get 方法
 * </p>
 * <p>
 * 如果是 boolean 类型, 尝试取 is 方法
 * </p>
 * <p>
 * 否则直接取字段 - 不会尝试修改可读性, 如果可读性有问题, 请主动get 然后修改
 * </p>
 *
 * @author lingting 2022/12/6 13:04
 */
@RequiredArgsConstructor
@SuppressWarnings("java:S3011")
public class ClassField {

	private final Field field;

	private final Method methodGet;

	private final Method methodSet;

	public String getFiledName() {
		return field.getName();
	}

	/**
	 * 是否拥有指定注解, 会同时对 字段 和 方法进行判断
	 * @param a 注解类型
	 * @return boolean true 表示拥有
	 */
	public <T extends Annotation> T getAnnotation(Class<T> a) {
		T annotation;
		// 字段上找到了
		if (field != null && (annotation = field.getAnnotation(a)) != null) {
			return annotation;
		}
		// 方法上找
		return methodGet == null ? null : methodGet.getAnnotation(a);
	}

	/**
	 * 获取字段值, 仅支持无参方法
	 * @param obj 对象
	 * @return java.lang.Object 对象指定字段值
	 */
	public Object get(Object obj) throws IllegalAccessException, InvocationTargetException {
		if (methodGet != null) {
			return methodGet.invoke(obj);
		}
		return field.get(obj);
	}

	/**
	 * 设置字段值
	 * @param obj 对象
	 * @param args set方法参数, 如果无set方法, 则第一个参数会被作为值通过字段设置
	 */
	public void set(Object obj, Object... args) throws InvocationTargetException, IllegalAccessException {
		if (methodSet != null) {
			methodSet.invoke(obj, args);
			return;
		}
		field.set(obj, args[0]);
	}

	public Class<?> getValueType() {
		return field == null ? methodGet.getReturnType() : field.getType();
	}

	// region get

	public Field field() {
		return field;
	}

	public Method methodGet() {
		return methodGet;
	}

	public Method methodSet() {
		return methodSet;
	}

	// endregion

}
