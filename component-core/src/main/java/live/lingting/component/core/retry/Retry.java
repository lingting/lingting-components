package live.lingting.component.core.retry;

import live.lingting.component.core.function.ThrowingSupplier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author lingting 2023-10-23 19:14
 */
@Slf4j
@Getter
public class Retry<T> {

	private final ThrowingSupplier<T> supplier;

	private final List<RetryLog<T>> logs = new ArrayList<>();

	/**
	 * 当前执行次数
	 */
	private int count = 0;

	/**
	 * 最大执行次数
	 */
	@Setter
	private int maxCount = 3;

	/**
	 * 重试延迟
	 */
	@Setter
	private Duration delay = Duration.ZERO;

	/**
	 * 传入异常, 返回是否重试. 默认非线程中断请求均重试
	 */
	@Setter
	private Predicate<Exception> isRetry = e -> !(e instanceof InterruptedException);

	public Retry(ThrowingSupplier<T> supplier) {
		this(supplier, 3, Duration.ZERO);
	}

	public Retry(ThrowingSupplier<T> supplier, int maxCount, Duration delay) {
		this.supplier = supplier;
		this.maxCount = maxCount;
		this.delay = delay;
	}

	public T get() throws Exception {
		return value().get();
	}

	public RetryValue<T> value() {
		long millis = delay.toMillis();
		boolean retry = false;
		T t = null;

		do {
			// 超出执行次数
			if (count >= getMaxCount()) {
				return new RetryValue<>(null, false, logs);
			}

			try {
				if (retry && millis > 0) {
					Thread.sleep(millis);
				}

				t = supplier.get();
				retry = false;
				logs.add(new RetryLog<>(t, null));
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				retry = isRetry.test(e);
				logs.add(new RetryLog<>(null, e));
			}
			catch (Exception e) {
				retry = isRetry.test(e);
				logs.add(new RetryLog<>(null, e));
			}
			finally {
				count += 1;
			}
		}
		while (retry);

		return new RetryValue<>(t, true, logs);
	}

	public int getMaxCount() {
		// 保证至少执行一次
		return Math.max(1, maxCount);
	}

}
