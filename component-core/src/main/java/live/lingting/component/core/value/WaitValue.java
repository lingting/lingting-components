package live.lingting.component.core.value;

import live.lingting.component.core.lock.JavaReentrantLock;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.core.util.ValueUtils;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * @author lingting 2023-05-21 20:13
 */
public class WaitValue<T> {

	protected final JavaReentrantLock lock = new JavaReentrantLock();

	@Getter
	protected T value;

	public static <T> WaitValue<T> of() {
		return new WaitValue<>();
	}

	public static <T> WaitValue<T> of(T t) {
		WaitValue<T> of = of();
		of.value = t;
		return of;
	}

	public void update(T t) throws InterruptedException {
		update(v -> t);
	}

	public void update(UnaryOperator<T> operator) throws InterruptedException {
		lock.runByInterruptibly(() -> {
			value = operator.apply(value);
			lock.signalAll();
		});
	}

	/**
	 * 进行运算, 同时仅允许一个线程获取
	 * @param operator 运行行为
	 */
	public synchronized T compute(UnaryOperator<T> operator) throws InterruptedException {
		return lock.getByInterruptibly(() -> {
			T v = operator.apply(value);
			update(v);
			return v;
		});
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
			return ValueUtils.await(() -> value, predicate, () -> lock.await(1, TimeUnit.HOURS));
		}
		finally {
			lock.unlock();
		}
	}

	public boolean isNull() {
		return value == null;
	}

}
