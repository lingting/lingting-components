package live.lingting.component.core.thread;

import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2022/11/17 20:15
 */
@Slf4j
@AllArgsConstructor
@SuppressWarnings("java:S6548")
public class ThreadPool {

	protected static final ThreadPool THREAD_POOL;

	protected static final Integer QUEUE_MAX = 100;

	@Getter
	protected ThreadPoolExecutor pool;

	static {
		THREAD_POOL = new ThreadPool(new ThreadPoolExecutor(
				// 核心线程数大小. 不论是否空闲都存在的线程
				300,
				// 最大线程数 - 10万个
				100000,
				// 存活时间. 非核心线程数如果空闲指定时间. 就回收
				// 存活时间不宜过长. 避免任务量遇到尖峰情况时. 大量空闲线程占用资源
				10,
				// 存活时间的单位
				TimeUnit.SECONDS,
				// 等待任务存放队列 - 队列最大值
				// 这样配置. 当积压任务数量为 队列最大值 时. 会创建新线程来执行任务. 直到线程总数达到 最大线程数
				new LinkedBlockingQueue<>(QUEUE_MAX),
				// 新线程创建工厂 - LinkedBlockingQueue 不支持线程优先级. 所以直接新增线程就可以了
				runnable -> new Thread(null, runnable),
				// 拒绝策略 - 在主线程继续执行.
				new ThreadPoolExecutor.CallerRunsPolicy()));
	}

	public static ThreadPool instance() {
		return THREAD_POOL;
	}

	public static ThreadPool update(ThreadPoolExecutor executor) {
		ThreadPool instance = instance();
		instance.pool = executor;
		return instance;
	}

	/**
	 * 线程池是否活跃
	 */
	public boolean isRunning() {
		return getCount() > 0;
	}

	/**
	 * 线程当前活跃数量
	 */
	public long getCount() {
		return getPool().getTaskCount();
	}

	public void execute(ThrowingRunnable runnable) {
		execute(null, runnable);
	}

	public void execute(String name, ThrowingRunnable runnable) {
		getPool().execute(() -> {
			Thread thread = Thread.currentThread();
			String oldName = thread.getName();
			if (StringUtils.hasText(name)) {
				thread.setName(name);
			}

			try {
				runnable.run();
			}
			catch (InterruptedException e) {
				thread.interrupt();
				log.warn("线程池内部线程被中断!");
			}
			catch (Throwable throwable) {
				log.error("线程内部线程异常!", throwable);
			}
			finally {
				thread.setName(oldName);
			}
		});
	}

}
