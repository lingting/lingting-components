package live.lingting.component.okhttp.proxy;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.core.constant.GlobalConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author lingting 2023-04-09 23:17
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ProxyConfig {

	private final String host;

	private final Integer port;

	private final String username;

	private final char[] password;

	/**
	 * 过期时间戳
	 */
	private final long expireTime;

	private final Proxy proxy;

	public static ProxyConfig http(String host, Integer port, long expireTime) {
		return http(host, port, null, null, expireTime);
	}

	public static ProxyConfig http(String host, Integer port, String username, char[] password, long expireTime) {
		return new ProxyConfig(host, port, username, password, expireTime,
				new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port)));
	}

	public static ProxyConfig socks(String host, Integer port, long expireTime) {
		return socks(host, port, null, null, expireTime);
	}

	public static ProxyConfig socks(String host, Integer port, String username, char[] password, long expireTime) {
		return new ProxyConfig(host, port, username, password, expireTime,
				new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port)));
	}

	public static ProxyConfig of(Proxy.Type type, String host, Integer port, long expireTimeout) {
		if (Proxy.Type.HTTP.equals(type)) {
			return http(host, port, expireTimeout);
		}
		return socks(host, port, expireTimeout);
	}

	public Socket socket() {
		return new Socket(proxy);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ProxyConfig that = (ProxyConfig) o;
		return Objects.equals(host, that.host) && Objects.equals(port, that.port)
				&& Objects.equals(username, that.username) && Arrays.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(host, port, username);
		result = 31 * result + Arrays.hashCode(password);
		return result;
	}

	/**
	 * username:password@host:port
	 */
	public String string() {
		StringBuilder builder = new StringBuilder();
		if (StringUtils.hasText(username)) {
			builder.append(username).append(GlobalConstants.COLON).append(password).append("@");
		}
		builder.append(host).append(GlobalConstants.COLON).append(port);
		return builder.toString();
	}

}
