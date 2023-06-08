package live.lingting.component.core;

import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.core.value.WaitValue;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-06-05 17:31
 */
@Slf4j
@Getter
public class Async {

	@Setter
	protected static ThreadPool defaultPool = ThreadPool.instance();

	protected final WaitValue<Integer> counter = WaitValue.of(0);

	@Setter
	protected ThreadPool pool = defaultPool;

	public void submit(String name, ThrowingRunnable runnable) throws InterruptedException {
		increment();
		pool.execute(String.format("Async-%s", name), new Runnable() {
			@SneakyThrows
			@Override
			public void run() {
				try {
					runnable.run();
				}
				catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					log.error("异步任务被中断!");
				}
				catch (Exception e) {
					log.error("异步任务执行异常!", e);
				}
				finally {
					decrement();
				}
			}
		});
	}

	protected void increment() throws InterruptedException {
		counter.update(v -> v + 1);
	}

	protected void decrement() throws InterruptedException {
		counter.update(v -> v - 1);
	}

	public void await() throws InterruptedException {
		counter.wait(v -> v < 1);
	}

	public long count() {
		return counter.getValue();
	}

}
