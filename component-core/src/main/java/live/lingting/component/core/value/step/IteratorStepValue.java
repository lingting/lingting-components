package live.lingting.component.core.value.step;

import live.lingting.component.core.value.StepValue;
import lombok.SneakyThrows;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingting 2024-01-23 15:30
 */
public class IteratorStepValue<T> extends AbstractConcurrentStepValue<T> {

	private final Map<BigInteger, T> map;

	private final Iterator<T> iterator;

	public IteratorStepValue(Iterator<T> iterator) {
		this(new ConcurrentHashMap<>(), iterator);
	}

	protected IteratorStepValue(Map<BigInteger, T> map, Iterator<T> iterator) {
		this.map = map;
		this.iterator = iterator;
	}

	@Override
	public StepValue<T> copy() {
		return new IteratorStepValue<>(map, iterator);
	}

	@Override
	public boolean doHasNext() {
		// 下一个已经取出来了, 为true
		if (map.containsKey(index.add(BigInteger.ONE))) {
			return true;
		}
		// 没取出来, 用下一个值
		return iterator.hasNext();
	}

	@Override
	public T doNext() {
		return map.computeIfAbsent(increasing(), i -> iterator.next());
	}

	@Override
	public T doCalculate(BigInteger index) {
		if (map.containsKey(index)) {
			return map.get(index);
		}

		// 如果迭代器已经取空了
		if (!iterator.hasNext()) {
			return null;
		}

		// 没取空, 接着取
		StepValue<T> value = copy();
		while (value.hasNext()) {
			T t = value.next();
			if (index.compareTo(value.index()) == 0) {
				return t;
			}
		}
		return null;
	}

	@Override
	@SneakyThrows
	public void remove() {
		lock.runByInterruptibly(() -> {
			// 至少需要调用一次next
			if (index.compareTo(BigInteger.ZERO) == 0) {
				throw new IllegalStateException();
			}
			// 移除当前索引位置的值
			map.remove(index);
			// index 回调
			index = index.subtract(BigInteger.ONE);
		});
	}

}
