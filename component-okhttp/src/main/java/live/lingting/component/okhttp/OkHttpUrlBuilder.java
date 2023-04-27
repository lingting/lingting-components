package live.lingting.component.okhttp;

import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.okhttp.constant.HttpConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lingting 2023-04-05 20:43
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OkHttpUrlBuilder {

	private String host;

	private Integer port;

	private String uri;

	private Map<String, List<String>> params = new HashMap<>();

	public static OkHttpUrlBuilder builder() {
		return new OkHttpUrlBuilder();
	}

	public HttpUrl build() {
		StringBuilder builder = new StringBuilder(host);
		if (port != null) {
			builder.append(HttpConstants.URL_DELIMITER).append(port);
		}
		// 不是 / 结尾
		if (builder.charAt(builder.length() - 1) != HttpConstants.HOST_DELIMITER_CHAR) {
			builder.append(HttpConstants.HOST_DELIMITER);
		}
		// uri 以 / 开头
		if (uri.startsWith(HttpConstants.HOST_DELIMITER)) {
			builder.append(uri, 1, uri.length() - 1);
		}
		else {
			builder.append(uri);
		}

		// 存在参数
		if (!CollectionUtils.isEmpty(params)) {
			// 非 ? 结尾
			if (!uri.endsWith(HttpConstants.QUERY_DELIMITER)) {
				builder.append(HttpConstants.QUERY_DELIMITER);
			}

			// 拼接参数
			for (Map.Entry<String, List<String>> entry : params.entrySet()) {
				for (String value : entry.getValue()) {
					builder.append(HttpConstants.QUERY_PARAMS_DELIMITER)
						.append(entry.getKey())
						.append("=")
						.append(value);
				}
			}
		}

		String url = builder.toString();
		return HttpUrl.parse(url);
	}

	public OkHttpUrlBuilder host(String host) {
		this.host = host;
		return this;
	}

	public OkHttpUrlBuilder port(Integer port) {
		this.port = port;
		return this;
	}

	public OkHttpUrlBuilder uri(String uri) {
		this.uri = uri;
		return this;
	}

	public OkHttpUrlBuilder addParam(String name, String value) {
		params.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
		return this;
	}

}
