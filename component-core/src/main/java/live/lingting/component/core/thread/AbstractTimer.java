package live.lingting.component.core.thread;

import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2022/6/27 20:26
 */
public abstract class AbstractTimer extends AbstractThreadContextComponent {

	/**
	 * 获取超时时间, 单位: 毫秒
	 */
	public long getTimeout() {
		return TimeUnit.SECONDS.toMillis(30);
	}

	/**
	 * 执行任务
	 */
	@SuppressWarnings("java:S112")
	protected abstract void process() throws Exception;

	protected void doRun() throws Exception {
		try {
			process();
		}
		finally {
			Thread.sleep(getTimeout());
		}
	}

}
