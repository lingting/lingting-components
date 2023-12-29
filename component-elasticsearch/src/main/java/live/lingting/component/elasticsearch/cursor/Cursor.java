package live.lingting.component.elasticsearch.cursor;

import live.lingting.component.core.util.CollectionUtils;
import lombok.Getter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author lingting 2023-12-29 11:30
 */
public abstract class Cursor<T> implements Iterator<T> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected final List<T> current = new ArrayList<>();

	/**
	 * 是否已经无数据了, 如果为true, 则不会继续调用 {@link Cursor#nextBatchData()}方法, 且
	 * {@link Cursor#hasNext()}方法永远返回false
	 */
	protected boolean empty = false;

	/**
	 * 已读取数据数量
	 */
	@Getter
	protected long count = 0;

	@Override
	public boolean hasNext() {
		if (!CollectionUtils.isEmpty(current)) {
			return true;
		}

		if (empty) {
			return false;
		}

		List<T> list = nextBatchData();

		if (CollectionUtils.isEmpty(list)) {
			empty = true;
			return false;
		}

		current.addAll(list);
		return true;
	}

	@Override
	public T next() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		count++;
		return current.remove(0);
	}

	/**
	 * 获取下一批数据
	 */
	protected abstract List<T> nextBatchData();

	public Stream<T> stream() {
		Spliterator<T> spliterator = Spliterators.spliteratorUnknownSize(this, Spliterator.ORDERED);
		return StreamSupport.stream(spliterator, false);
	}

}
