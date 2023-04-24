package live.lingting.component.okhttp.proxy;

import live.lingting.component.core.thread.AbstractTimer;
import live.lingting.component.core.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-05 13:47
 */
public abstract class AbstractProxyTimer extends AbstractTimer {

	protected final List<ProxyPool> pools = new CopyOnWriteArrayList<>();

	@Override
	public long getTimeout() {
		return TimeUnit.SECONDS.toMillis(10);
	}

	@Override
	protected void process() throws Exception {
		Collection<ProxyConfig> configs = configs();
		for (ProxyPool pool : pools) {
			pool.reset(configs);
		}
	}

	protected abstract void putAll(ProxyPool pool, Collection<ProxyConfig> configs);

	protected abstract Collection<ProxyConfig> configs();

	public void add(ProxyPool pool) {
		pools.add(pool);
	}

	public ProxyPool getFirst() {
		return CollectionUtils.isEmpty(pools) ? null : pools.get(0);
	}

	public void remove(ProxyPool pool) {
		pools.remove(pool);
	}

	public void clear() {
		pools.clear();
	}

}
