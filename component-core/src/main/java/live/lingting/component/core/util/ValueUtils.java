package live.lingting.component.core.util;

import live.lingting.component.core.function.ThrowingRunnable;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author lingting 2023-12-20 11:36
 */
@UtilityClass
public class ValueUtils {

	/**
	 * 等待值满足条件, 不满足条件休眠 500 毫秒
	 * @param supplier 值获取
	 * @param predicate 值条件测试, 返回true表示该值为目标值, 当前函数会返回该值
	 * @return 值
	 * @param <T> 值类型
	 */
	public static <T> T await(Supplier<T> supplier, Predicate<T> predicate) {
		return await(supplier, predicate, () -> Thread.sleep(500));
	}

	/**
	 * 等待值满足条件
	 * @param supplier 值获取
	 * @param predicate 值条件测试, 返回true表示该值为目标值, 当前函数会返回该值
	 * @param sleep 休眠
	 * @return 值
	 * @param <T> 值类型
	 */
	@SneakyThrows
	public static <T> T await(Supplier<T> supplier, Predicate<T> predicate, ThrowingRunnable sleep) {
		while (true) {
			T t = supplier.get();
			if (predicate.test(t)) {
				return t;
			}
			sleep.run();
		}
	}

}
