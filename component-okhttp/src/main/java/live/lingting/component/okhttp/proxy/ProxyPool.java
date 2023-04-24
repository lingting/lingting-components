package live.lingting.component.okhttp.proxy;

import live.lingting.component.core.queue.WaitQueue;
import live.lingting.component.okhttp.exception.OkHttpException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.Authenticator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author lingting 2023/1/30 11:31
 */
@Slf4j
public class ProxyPool {

	private final ThreadLocal<ProxyConfig> threadLocal = new ThreadLocal<>();

	private final WaitQueue<ProxyConfig> queue;

	@Setter
	private Supplier<Long> currentTimeMillisSupplier = System::currentTimeMillis;

	private Set<ProxyConfig> proxies;

	private ProxyPool(WaitQueue<ProxyConfig> queue, Set<ProxyConfig> proxies) {
		this.queue = queue;
		this.proxies = proxies;
	}

	public static ProxyPool of(Collection<ProxyConfig> configs) {
		Authenticator.setDefault(ProxyAuthenticator.INSTANCE);
		ProxyPool pool = new ProxyPool(new WaitQueue<>(), new HashSet<>(configs.size()));
		for (ProxyConfig config : configs) {
			pool.add(config);
		}
		return pool;
	}

	public void reset(Collection<ProxyConfig> configs) {
		HashSet<ProxyConfig> set = new HashSet<>(configs.size());
		for (ProxyConfig config : configs) {
			add(config);
			set.add(config);
		}
		proxies = set;
	}

	public void add(ProxyConfig config) {
		if (!proxies.add(config)) {
			return;
		}
		queue.add(config);
	}

	public void addAll(Collection<ProxyConfig> configs) {
		for (ProxyConfig config : configs) {
			add(config);
		}
	}

	public ProxyConfig use(ProxyConfig config) {
		threadLocal.set(config);
		ProxyAuthenticator.set(this);
		return config;
	}

	/**
	 * 设置当前线程请求代理
	 */
	@SuppressWarnings("java:S135")
	public ProxyConfig proxy() {
		ProxyConfig config = threadLocal.get();
		// 当前线程已获取, 则直接用
		if (config != null) {
			return config;
		}
		long timeout;
		while (true) {
			config = getProxyConfig();
			if (!proxies.contains(config)) {
				continue;
			}
			timeout = config.getExpireTime() - currentTimeMillisSupplier.get();
			// 如果配置的过期时间小于1 或者 剩余时间大于1s则使用
			if (config.getExpireTime() < 1 || timeout >= 1000) {
				break;
			}
			// 否则表示该配置已失效. 移除
			proxies.remove(config);
		}
		return use(config);
	}

	private ProxyConfig getProxyConfig() {
		ProxyConfig config;
		try {
			config = queue.poll();
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new OkHttpException("从代理池中获取代理IP被打断!");
		}
		return config;
	}

	/**
	 * 获取当前线程请求代理
	 */
	public ProxyConfig get() {
		return threadLocal.get();
	}

	/**
	 * 释放当前线程请求代理
	 */
	public void release() {
		ProxyConfig config = threadLocal.get();
		if (config == null) {
			return;
		}
		threadLocal.remove();
		ProxyAuthenticator.remove();
		// 还在列表
		if (proxies.contains(config)) {
			queue.add(config);
		}
	}

	public Integer size() {
		return proxies.size();
	}

}
