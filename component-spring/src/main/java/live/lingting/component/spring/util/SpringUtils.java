package live.lingting.component.spring.util;

import live.lingting.component.core.util.ClassUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lingting 2022/10/15 15:21
 */
@UtilityClass
public class SpringUtils {

	private static final Map<Class<?>, Object> BEAN_CLS_CACHE = new HashMap<>();

	private static final Map<String, Object> BEAN_NAME_CACHE = new HashMap<>();

	@Setter
	@Getter
	private static ApplicationContext context;

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		if (BEAN_NAME_CACHE.containsKey(name)) {
			return (T) BEAN_NAME_CACHE.get(name);
		}
		Object bean = context.getBean(name);
		BEAN_NAME_CACHE.put(name, bean);
		return (T) bean;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz) {
		if (BEAN_CLS_CACHE.containsKey(clazz)) {
			return (T) BEAN_CLS_CACHE.get(clazz);
		}
		T bean = context.getBean(clazz);
		BEAN_CLS_CACHE.put(clazz, bean);
		return bean;
	}

	public static <T> T getBean(String name, Class<T> clazz) {
		return context.getBean(name, clazz);
	}

	public static <T> Map<String, T> getBeansOfType(Class<T> type) {
		return context.getBeansOfType(type);
	}

	public static String[] getBeanNamesForType(Class<?> type) {
		return context.getBeanNamesForType(type);
	}

	public static <T> T ofBean(Class<T> cls)
			throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Constructor<T>[] constructors = ClassUtils.constructors(cls);
		return ofBean(constructors[0], SpringUtils::getBean);
	}

	public static <T> T ofBean(Constructor<T> constructor, Function<Class<?>, Object> getArgument)
			throws InvocationTargetException, InstantiationException, IllegalAccessException {
		Class<?>[] types = constructor.getParameterTypes();
		List<Object> arguments = new ArrayList<>();

		for (Class<?> cls : types) {
			Object argument = getArgument.apply(cls);
			arguments.add(argument);
		}

		return constructor.newInstance(arguments.toArray());
	}

}
