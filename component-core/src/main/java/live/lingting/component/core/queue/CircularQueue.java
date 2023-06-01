package live.lingting.component.core.queue;

import live.lingting.component.core.lock.JavaReentrantLock;
import live.lingting.component.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 循环队列
 *
 * @author lingting 2023-05-30 10:25
 */
public class CircularQueue<T> {

	private final JavaReentrantLock lock = new JavaReentrantLock();

	private final List<T> source = new ArrayList<>();

	private Iterator<T> iterator;

	public CircularQueue<T> add(T t) throws InterruptedException {
		lock.runByInterruptibly(() -> source.add(t));
		return this;
	}

	public CircularQueue<T> addAll(Collection<T> collection) throws InterruptedException {
		lock.runByInterruptibly(() -> source.addAll(collection));
		return this;
	}

	public T pool() throws InterruptedException {
		return lock.getByInterruptibly(() -> {
			if (CollectionUtils.isEmpty(source)) {
				return null;
			}
			if (iterator == null || !iterator.hasNext()) {
				iterator = source.iterator();
			}

			return iterator.next();
		});
	}

}
