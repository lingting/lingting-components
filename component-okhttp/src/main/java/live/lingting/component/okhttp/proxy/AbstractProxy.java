package live.lingting.component.okhttp.proxy;

import java.util.Collections;

/**
 * @author lingting 2023-04-05 21:01
 */
public abstract class AbstractProxy {

	public abstract AbstractProxyTimer getTimer();

	// region 代理池方法

	/**
	 * 创建新的代理池
	 */
	public ProxyPool poolNew() {
		ProxyPool proxyPool = ProxyPool.of(Collections.emptyList());
		getTimer().add(proxyPool);
		getTimer().onApplicationStart();
		return proxyPool;
	}

	/**
	 * 复用第一个代理池, 没有则新建
	 */
	public synchronized ProxyPool pool() {
		ProxyPool first = getTimer().getFirst();
		if (first != null) {
			return first;
		}
		return poolNew();
	}

	public void poolRemove(ProxyPool pool) {
		getTimer().remove(pool);
	}

	public void poolClear() {
		getTimer().clear();
	}

	// endregion

}
