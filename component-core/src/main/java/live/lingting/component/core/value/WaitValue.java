package live.lingting.component.core.value;

import live.lingting.component.core.lock.JavaReentrantLock;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.StringUtils;
import lombok.SneakyThrows;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * @author lingting 2023-05-21 20:13
 */
public class WaitValue<T> {

	protected final JavaReentrantLock lock = new JavaReentrantLock();

	protected T value;

	public static <T> WaitValue<T> of() {
		return new WaitValue<>();
	}

	public static <T> WaitValue<T> of(T t) {
		WaitValue<T> of = of();
		of.value = t;
		return of;
	}

	@SneakyThrows
	public void update(T t) {
		value = t;
		lock.signalAll();
	}

	public T notNull() throws InterruptedException {
		return wait(Objects::nonNull);
	}

	public T notEmpty() throws InterruptedException {
		return wait(v -> {
			if (v == null) {
				return false;
			}

			if (v instanceof Collection) {
				return !CollectionUtils.isEmpty((Collection<?>) v);
			}
			else if (v instanceof Map) {
				return !CollectionUtils.isEmpty((Map<?, ?>) v);
			}
			else if (v instanceof String) {
				return StringUtils.hasText((CharSequence) v);
			}

			return true;
		});
	}

	public T wait(Predicate<T> predicate) throws InterruptedException {
		lock.lockInterruptibly();
		try {
			while (true) {
				if (predicate.test(value)) {
					return value;
				}

				lock.await(1, TimeUnit.HOURS);
			}
		}
		finally {
			lock.unlock();
		}
	}

}
