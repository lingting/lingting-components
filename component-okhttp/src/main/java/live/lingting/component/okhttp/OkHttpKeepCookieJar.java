package live.lingting.component.okhttp;

import live.lingting.component.core.util.CollectionUtils;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author lingting 2022/11/17 16:42
 */
public class OkHttpKeepCookieJar implements CookieJar {

	private final Map<String, List<Cookie>> cache = new ConcurrentHashMap<>();

	@NotNull
	@Override
	public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
		String host = httpUrl.host();
		String privateDomain = httpUrl.topPrivateDomain();

		List<Cookie> hostCookies = cache.get(host);
		List<Cookie> privateDomainCookies = cache.get(privateDomain);

		List<Cookie> cookies = new ArrayList<>();
		if (!CollectionUtils.isEmpty(hostCookies)) {
			cookies.addAll(hostCookies);
		}
		if (!CollectionUtils.isEmpty(privateDomainCookies)) {
			cookies.addAll(privateDomainCookies);
		}
		return cookies;
	}

	@Override
	public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
		Map<String, List<Cookie>> map = new HashMap<>();

		for (Cookie cookie : list) {
			List<Cookie> cookies = map.computeIfAbsent(cookie.domain(), k -> new ArrayList<>());
			cookies.add(cookie);
		}

		for (Map.Entry<String, List<Cookie>> entry : map.entrySet()) {
			addAll(entry.getKey(), entry.getValue());
		}
	}

	public void addAll(String domain, Collection<Cookie> collection) {
		cache.compute(domain, (k, val) -> {
			List<Cookie> cookies = val == null ? new ArrayList<>() : val;
			cookies.addAll(collection);
			return cookies;
		});
	}

	public void add(Cookie cookie) {
		addAll(cookie.domain(), Collections.singleton(cookie));
	}

	public Map<String, List<Cookie>> all() {
		return Collections.unmodifiableMap(cache);
	}

	public void clean(String domain) {
		cache.remove(domain);
	}

}
