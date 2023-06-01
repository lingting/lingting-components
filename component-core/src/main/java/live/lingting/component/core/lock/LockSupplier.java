package live.lingting.component.core.lock;

/**
 * @author lingting 2023-04-22 11:35
 */
public interface LockSupplier<R> {

	R get() throws InterruptedException;

}
