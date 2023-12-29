package live.lingting.component.core.value;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

/**
 * 循环数据
 *
 * @author lingting 2023-12-19 16:01
 */
@Getter
@RequiredArgsConstructor
public class CycleValue<T> {

	protected final CycleValueFunction<T> function;

	/**
	 * 已执行次数
	 */
	protected BigInteger count = BigInteger.ZERO;

	public static CycleValue<Long> ofStep(StepValue<Long> stepValue) {
		StepValue<Long> value = stepValue.copy();
		return new CycleValue<>(new CycleValueFunction<Long>() {
			@Override
			public boolean hasNext() {
				return value.hasNext();
			}

			@Override
			public Long next() {
				return value.next();
			}

			@Override
			public void reset() {
				value.reset();
			}
		});
	}

	public T next() {
		if (!function.hasNext()) {
			function.reset();
		}
		return function.next();
	}

	public interface CycleValueFunction<T> {

		/**
		 * 是否存在下一个元素, 如果返回false, 则会调用 {@link CycleValueFunction#reset()}
		 * @return true 存在下一个元素
		 */
		boolean hasNext();

		/**
		 * 获取下一个元素
		 * @return 下一个
		 */
		T next();

		/**
		 * 重置元素, 重新从第一个开始
		 */
		void reset();

	}

}
