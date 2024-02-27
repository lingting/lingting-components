package live.lingting.component.core.value.cycle;

import live.lingting.component.core.lock.JavaReentrantLock;
import lombok.SneakyThrows;

/**
 * @author lingting 2024-02-27 19:19
 */
public abstract class AbstractConcurrentCycleValue<T> extends AbstractCycleValue<T> {

	protected final JavaReentrantLock lock = new JavaReentrantLock();

	@Override
	@SneakyThrows
	public void reset() {
		lock.runByInterruptibly(this::doReset);
	}

	@Override
	@SneakyThrows
	public T next() {
		return lock.getByInterruptibly(super::next);
	}

	public abstract void doReset();

}
