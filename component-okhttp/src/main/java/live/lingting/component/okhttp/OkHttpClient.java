package live.lingting.component.okhttp;

import live.lingting.component.core.function.ThrowingFunction;
import live.lingting.component.jackson.JacksonUtils;
import live.lingting.component.okhttp.exception.OkHttpException;
import live.lingting.component.okhttp.proxy.ProxyConfig;
import live.lingting.component.okhttp.proxy.ProxyPool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

/**
 * @author lingting 2023/1/31 13:59
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings("java:S1700")
public class OkHttpClient {

	public static OkHttpClientBuilder builder() {
		return new OkHttpClientBuilder();
	}

	protected final okhttp3.OkHttpClient client;

	protected final ProxyPool pool;

	public Response request(Request request) {
		if (pool != null) {
			return callByProxy(request);
		}

		Call call = client.newCall(request);
		try {
			return call.execute();
		}
		catch (IOException e) {
			throw new OkHttpException(e);
		}
	}

	Response callByProxy(Request request) {
		ProxyConfig config = pool.proxy();
		okhttp3.OkHttpClient proxyClient = this.client.newBuilder().proxy(config.getProxy()).build();
		Call call = proxyClient.newCall(request);
		try {
			return call.execute();
		}
		catch (IOException e) {
			throw new OkHttpException(e);
		}
		finally {
			pool.release();
		}
	}

	// region 请求用
	public <T> T request(Request request, ThrowingFunction<Response, T> function) {
		try (Response response = request(request)) {
			return function.apply(response);
		}
		catch (OkHttpException e) {
			throw e;
		}
		catch (Exception e) {
			throw new OkHttpException("返回值处理异常!", e);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> T request(Request request, Class<T> cls) {
		return request(request, response -> {
			ResponseBody responseBody = response.body();
			if (responseBody == null) {
				return null;
			}

			String string = responseBody.string();
			if (cls.isAssignableFrom(String.class)) {
				return (T) string;
			}
			return JacksonUtils.toObj(string, cls);
		});
	}

	public Response get(String url) {
		Request.Builder builder = new Request.Builder().url(url).get();
		return request(builder.build());
	}

	public <T> T get(String url, Class<T> cls) {
		Request.Builder builder = new Request.Builder().url(url).get();
		return request(builder.build(), cls);
	}

	public Response get(HttpUrl url) {
		Request.Builder builder = new Request.Builder().url(url).get();
		return request(builder.build());
	}

	public <T> T get(HttpUrl url, Class<T> cls) {
		Request.Builder builder = new Request.Builder().url(url).get();
		return request(builder.build(), cls);
	}

	public Response post(String url, RequestBody body) {
		Request.Builder builder = new Request.Builder().url(url).post(body);
		return request(builder.build());
	}

	public <T> T post(String url, RequestBody requestBody, Class<T> cls) {
		Request.Builder builder = new Request.Builder().url(url).post(requestBody);
		return request(builder.build(), cls);
	}

	// endregion

	// region 配置相关
	public okhttp3.OkHttpClient client() {
		return client;
	}

	public CookieJar cookieJar() {
		return client.cookieJar();
	}

	public OkHttpClientBuilder toBuilder() {
		return builder().okHttpClientBuilder(client.newBuilder());
	}

	// endregion

}
