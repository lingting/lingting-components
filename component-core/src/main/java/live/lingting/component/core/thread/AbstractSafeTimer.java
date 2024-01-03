package live.lingting.component.core.thread;

/**
 * @author lingting 2022/6/27 20:26
 */
public abstract class AbstractSafeTimer extends AbstractTimer {

	@Override
	public void onApplicationStopBefore() {
		awaitTerminated();
	}

}
