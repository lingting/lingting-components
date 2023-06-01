package live.lingting.component.core.thread;

import live.lingting.component.core.lock.JavaReentrantLock;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-22 10:39
 */
@Slf4j
@SuppressWarnings("java:S1066")
public abstract class AbstractDynamicTimer<T> extends AbstractThreadContextComponent {

	protected final JavaReentrantLock lock = new JavaReentrantLock();

	protected final PriorityQueue<T> queue = new PriorityQueue<>(comparator());

	public abstract Comparator<T> comparator();

	/**
	 * 还有多久要处理该对象
	 * @param t 对象
	 * @return 具体处理该对象还要多久, 单位: 毫秒
	 */
	protected abstract long sleepTime(T t);

	public void put(T t) {
		if (t == null) {
			return;
		}

		try {
			lock.runByInterruptibly(() -> {
				queue.add(t);
				lock.signalAll();
			});
		}
		catch (InterruptedException e) {
			interrupt();
		}
		catch (Exception e) {
			log.error("{} put error, param: {}", this.getClass().toString(), t, e);
		}
	}

	/**
	 * 将取出的元素重新放入队列
	 */
	public void replay(T t) {
		put(t);
	}

	@Override
	public void run() {
		init();
		while (isRun()) {
			try {
				T t = pool();
				lock.runByInterruptibly(() -> {
					if (t == null) {
						lock.await(24, TimeUnit.HOURS);
						return;
					}

					long sleepTime = sleepTime(t);
					// 需要休眠
					if (sleepTime > 0) {
						// 如果是被唤醒
						if (lock.await(sleepTime, TimeUnit.MILLISECONDS)) {
							replay(t);
							return;
						}
					}

					process(t);
				});
			}
			catch (InterruptedException e) {
				interrupt();
				shutdown();
			}
			catch (Exception e) {
				error(e);
			}
		}
	}

	protected T pool() {
		return queue.poll();
	}

	protected abstract void process(T t);

	protected void error(Exception e) {
		log.error("类: {}; 线程: {}; 运行异常! ", getSimpleName(), getId(), e);
	}

	protected void shutdown() {
		log.warn("类: {}; 线程: {}; 被中断! 剩余数据: {}", getSimpleName(), getId(), queue.size());
	}

}
