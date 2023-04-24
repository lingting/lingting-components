package live.lingting.component.okhttp.proxy;

import live.lingting.component.core.util.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * @author lingting 2023/1/30 13:55
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProxyAuthenticator extends Authenticator {

	private static final ThreadLocal<ProxyPool> POOL_THREAD_LOCAL = new ThreadLocal<>();

	public static final ProxyAuthenticator INSTANCE = new ProxyAuthenticator();

	public static void set(ProxyPool pool) {
		POOL_THREAD_LOCAL.set(pool);
	}

	public static void remove() {
		POOL_THREAD_LOCAL.remove();
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		ProxyPool pool = POOL_THREAD_LOCAL.get();
		ProxyConfig config;
		if (pool == null || (config = pool.get()) == null) {
			return null;
		}
		if (!StringUtils.hasText(config.getUsername())) {
			return null;
		}
		return new PasswordAuthentication(config.getUsername(), config.getPassword());
	}

}
