package live.lingting.component.core.value.step;

import live.lingting.component.core.lock.JavaReentrantLock;
import live.lingting.component.core.value.StepValue;

/**
 * @author lingting 2024-01-15 19:21
 */
public class ConcurrentStepValue<T> extends StepValue<T> {

	private final JavaReentrantLock lock = new JavaReentrantLock();

	public ConcurrentStepValue(T startValue, StepFunction<T> function) {
		super(startValue, function);
	}

	@Override
	@SuppressWarnings("java:S2272")
	public T next() {
		return lock.get(super::next);
	}

	@Override
	public void reset() {
		lock.run(super::reset);
	}

	@Override
	public StepValue<T> copy() {
		return concurrent();
	}

	@Override
	public StepValue<T> start(T startValue) {
		return new ConcurrentStepValue<>(startValue, function);
	}

}
