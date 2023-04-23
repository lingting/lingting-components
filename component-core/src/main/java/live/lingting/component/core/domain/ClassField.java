package live.lingting.component.core.domain;

import lombok.Getter;
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
@Getter
@RequiredArgsConstructor
public class ClassField {

	private final Field field;

	private final Method method;

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
		return method == null ? null : method.getAnnotation(a);
	}

	/**
	 * 获取字段值, 仅支持无参方法
	 * @param obj 对象
	 * @return java.lang.Object 对象指定字段值
	 */
	public Object invoke(Object obj) throws IllegalAccessException, InvocationTargetException {
		Method invokeMethod = getMethod();
		if (invokeMethod != null) {
			return invokeMethod.invoke(obj);
		}
		return getField().get(obj);
	}

	public Class<?> getValueType() {
		return field == null ? method.getReturnType() : field.getType();
	}

}
