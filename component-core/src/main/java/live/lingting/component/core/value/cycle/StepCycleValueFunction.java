package live.lingting.component.core.value.cycle;

import live.lingting.component.core.value.StepValue;
import lombok.RequiredArgsConstructor;

/**
 * @author lingting 2024-01-23 15:22
 */
@RequiredArgsConstructor
public class StepCycleValueFunction<T> implements CycleValueFunction<T> {

	private final StepValue<T> value;

	@Override
	public boolean hasNext() {
		return value.hasNext();
	}

	@Override
	public T next() {
		return value.next();
	}

	@Override
	public void reset() {
		value.reset();
	}

}
