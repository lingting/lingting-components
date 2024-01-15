package live.lingting.component.core.value.step;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author lingting 2024-01-15 19:22
 */
@Getter
@RequiredArgsConstructor
public class SimpleStepFunction implements StepFunction<Long> {

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

	@Override
	public Long next(long count, Long previous) {
		// 超次数
		if (maxStepCount != null && count >= maxStepCount) {
			return null;
		}
		// 上一个值超限
		if (maxStepValue != null && previous != null && previous > maxStepValue) {
			return null;
		}
		// 获取下一个值
		long next = previous == null ? step : previous + step;
		// 下一个值超限
		if (maxStepValue != null && next > maxStepValue) {
			return null;
		}
		return next;
	}

}
