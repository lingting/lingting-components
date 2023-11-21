package live.lingting.component.core.util;

import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.thread.ThreadPool;
import lombok.experimental.UtilityClass;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Supplier;

/**
 * @author lingting 2023-11-15 16:44
 */
@UtilityClass
public class ThreadUtils {

	public static ThreadPool instance() {
		return ThreadPool.instance();
	}

	public static void execute(ThrowingRunnable runnable) {
		execute(null, runnable);
	}

	public static void execute(String name, ThrowingRunnable runnable) {
		instance().execute(name, runnable);
	}

	public static <T> CompletableFuture<T> async(Supplier<T> supplier) {
		return instance().async(supplier);
	}

	public <T> Future<T> submit(Callable<T> callable) {
		return instance().submit(callable);
	}

}
