package live.lingting.component.core.thread;

import live.lingting.component.core.ContextComponent;
import org.slf4j.Logger;

/**
 * @author lingting 2023-04-22 10:40
 */
public abstract class AbstractThreadContextComponent extends Thread implements ContextComponent {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected void init() {

	}

	public boolean isRun() {
		return !isInterrupted() && isAlive();
	}

	@Override
	public void onApplicationStart() {
		setName(getClass().getSimpleName());
		if (!isAlive()) {
			start();
		}
	}

	@Override
	public void onApplicationStop() {
		log.warn("{} 线程: {}; 开始关闭!", getClass().getSimpleName(), getId());
		interrupt();
	}

	public String getSimpleName() {
		return getClass().getSimpleName();
	}

	@Override
	public abstract void run();

}
