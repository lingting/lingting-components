package live.lingting.component.okhttp;

import live.lingting.component.core.constant.GlobalConstants;
import live.lingting.component.core.constant.HttpConstants;
import live.lingting.component.core.util.CollectionUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static live.lingting.component.core.constant.HttpConstants.HOST_DELIMITER;

/**
 * @author lingting 2023-04-05 20:43
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OkHttpUrlBuilder {

	private String host;

	private Integer port;

	private String uri;

	private final Map<String, List<String>> params = new HashMap<>();

	public static OkHttpUrlBuilder builder() {
		return new OkHttpUrlBuilder();
	}

	public HttpUrl build() {
		StringBuilder builder = new StringBuilder(host);
		if (host.endsWith(HOST_DELIMITER)) {
			builder.deleteCharAt(builder.length() - 1);
		}

		if (port != null) {
			builder.append(GlobalConstants.COLON).append(port);
		}

		// uri 是否以 / 开头
		if (!uri.startsWith(HOST_DELIMITER)) {
			builder.append(HOST_DELIMITER);
		}

		builder.append(uri);

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

	public OkHttpUrlBuilder addParams(Map<String, ?> params) {
		for (Map.Entry<String, ?> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value == null) {
				continue;
			}
			addParam(key, value.toString());
		}
		return this;
	}

}
