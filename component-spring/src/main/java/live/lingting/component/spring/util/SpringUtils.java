package live.lingting.component.spring.util;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author lingting 2022/10/15 15:21
 */
@UtilityClass
public class SpringUtils {

	@Setter
	@Getter
	private static ApplicationContext context;

	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) context.getBean(name);
	}

	public static <T> T getBean(Class<T> clazz) {
		return context.getBean(clazz);
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

}
