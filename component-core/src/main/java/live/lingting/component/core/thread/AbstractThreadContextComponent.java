package live.lingting.component.core.thread;

import live.lingting.component.core.context.ContextComponent;
import live.lingting.component.core.context.ContextHolder;
import org.slf4j.Logger;

/**
 * @author lingting 2023-04-22 10:40
 */
public abstract class AbstractThreadContextComponent extends Thread implements ContextComponent {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected void init() {

	}

	public boolean isRun() {
		return !isInterrupted() && isAlive() && !ContextHolder.isStop();
	}

	@Override
	public void onApplicationStart() {
		setName(getSimpleName());
		if (!isAlive()) {
			start();
		}
	}

	@Override
	public void onApplicationStop() {
		log.warn("{} 线程: {}; 开始关闭!", getSimpleName(), getId());
		interrupt();
	}

	public String getSimpleName() {
		return getClass().getSimpleName();
	}

	@Override
	public void run() {
		init();
		while (isRun()) {
			try {
				doRun();
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

	@SuppressWarnings("java:S112")
	protected abstract void doRun() throws Exception;

	/**
	 * 线程被中断触发.
	 */
	protected void shutdown() {
		log.warn("{} 类 线程: {} 被关闭.", getSimpleName(), getId());
	}

	protected void error(Exception e) {
		log.error("{} 类 线程: {} 出现异常!", getSimpleName(), getId(), e);
	}

}
