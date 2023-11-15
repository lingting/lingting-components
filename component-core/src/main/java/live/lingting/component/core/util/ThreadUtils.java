package live.lingting.component.core.util;

import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.thread.ThreadPool;
import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-11-15 16:44
 */
@UtilityClass
public class ThreadUtils {

	public static void execute(ThrowingRunnable runnable) {
		execute(null, runnable);
	}

	public static void execute(String name, ThrowingRunnable runnable) {
		ThreadPool instance = ThreadPool.instance();
		instance.execute(name, runnable);
	}

}
