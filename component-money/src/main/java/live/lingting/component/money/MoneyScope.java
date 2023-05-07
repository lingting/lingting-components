package live.lingting.component.money;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-05-07 18:00
 */
@UtilityClass
public class MoneyScope {

	private static final ThreadLocal<MoneyConfig> THREAD_LOCAL = new ThreadLocal<>();

	public static MoneyConfig get() {
		return THREAD_LOCAL.get();
	}

	public static void set(MoneyConfig config) {
		THREAD_LOCAL.set(config);
	}

	public static void remove() {
		THREAD_LOCAL.remove();
	}

}
