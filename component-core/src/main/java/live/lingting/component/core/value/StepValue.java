package live.lingting.component.core.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author lingting 2023-12-19 10:58
 */
@Getter
public class StepValue implements Iterator<Long> {

	protected final StepFunction function;

	/**
	 * 已获取值次数
	 */
	protected long count = 0;

	/**
	 * 上一次获取的值
	 */
	protected Long previous = null;

	/**
	 * 下一个值
	 */
	protected Long next;

	/**
	 * 简单步进值
	 * @param step 每次步进值
	 * @param maxStepCount 最大步进次数, 为null表示无限步进次数
	 * @param maxStepValue 最大步进值, 为null表示无最大值限制
	 */
	public static StepValue simple(long step, Long maxStepCount, Long maxStepValue) {
		return new SimpleStepValue(step, maxStepCount, maxStepValue);
	}

	public StepValue(StepFunction function) {
		this.function = function;
		this.next = getFirst();
	}

	@Override
	public boolean hasNext() {
		return next != null;
	}

	@Override
	public Long next() {
		// 如果下一个值为null,则不再进行步进了
		if (next == null) {
			throw new NoSuchElementException(String.format("count: %d, previous: %d", count, previous));
		}
		// 替换值
		previous = next;
		// 次数增加
		count++;
		// 获取下一个
		this.next = function.next(count, previous);
		// 返回当前值
		return previous;
	}

	public Long getFirst() {
		return function.next(0, null);
	}

	public StepValue copy() {
		return new StepValue(function);
	}

	public void reset() {
		this.count = 0;
		this.previous = null;
		this.next = getFirst();
	}

	@Getter
	public static class SimpleStepValue extends StepValue {

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
			super(new SimpleStepFunction(step, maxStepCount, maxStepValue));
			this.step = step;
			this.maxStepCount = maxStepCount;
			this.maxStepValue = maxStepValue;
		}

	}

	@FunctionalInterface
	public interface StepFunction {

		/**
		 * 获取下一个值
		 * @param count 当前已获取值的次数, 初始值为0
		 * @param previous 上一个获取的值, 初始值为 null
		 * @return 返回下一个获取的值, 为null表示结束
		 */
		Long next(long count, Long previous);

	}

	@Getter
	@RequiredArgsConstructor
	public static class SimpleStepFunction implements StepFunction {

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

}
