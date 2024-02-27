package live.lingting.component.core.value.cycle;

import live.lingting.component.core.value.CycleValue;
import live.lingting.component.core.value.step.IteratorStepValue;

import java.util.Iterator;

/**
 * @author lingting 2024-01-23 15:24
 */
public class IteratorCycleValue<T> extends CycleValue<T> {

	public IteratorCycleValue(Iterator<T> iterator) {
		super(new StepCycleValueFunction<>(new IteratorStepValue<>(iterator)));
	}

}
