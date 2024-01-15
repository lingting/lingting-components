package live.lingting.component.core.value.step;

import live.lingting.component.core.value.StepValue;
import lombok.Getter;

/**
 * 简单步进器, 第一个值为 startValue + step
 */
@Getter
public class SimpleStepValue extends StepValue<Long> {

	/**
	 * 每次步进值
	 */
	protected final long step;

	/**
	 * 最大步进次数, 为null表示无限步进次数
	 */
	protected final Long maxStepCount;

	/**
	 * 最大步进值, 为null表示无最大值限制
	 */
	protected final Long maxStepValue;

	public SimpleStepValue(long step, Long maxStepCount, Long maxStepValue) {
		super(0L, new SimpleStepFunction(step, maxStepCount, maxStepValue));
		this.step = step;
		this.maxStepCount = maxStepCount;
		this.maxStepValue = maxStepValue;
	}

}
