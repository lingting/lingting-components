package live.lingting.component.okhttp;

import live.lingting.component.okhttp.constant.HttpsConstants;
import live.lingting.component.okhttp.proxy.ProxyPool;
import lombok.Getter;
import okhttp3.CookieJar;

import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.time.Duration;
import java.util.function.UnaryOperator;

/**
 * @author lingting 2023/2/2 17:26
 */
@Getter
@SuppressWarnings("java:S1700")
public class OkHttpClientBuilder {

	private okhttp3.OkHttpClient.Builder okHttpClientBuilder = new okhttp3.OkHttpClient.Builder();

	private SocketFactory socketFactory;

	/**
	 * HostnameVerifier，用于HTTPS安全连接
	 */
	private HostnameVerifier hostnameVerifier;

	/**
	 * SSLSocketFactory，用于HTTPS安全连接
	 */
	private SSLSocketFactory sslSocketFactory;

	private X509TrustManager trustManager;

	private Duration callTimeout;

	private Duration connectTimeout;

	private Duration readTimeout;

	private Duration writeTimeout;

	private ProxyPool proxyPool = null;

	private CookieJar cookieJar = null;

	public okhttp3.OkHttpClient.Builder builder(okhttp3.OkHttpClient.Builder builder) {
		if (builder == null) {
			builder = new okhttp3.OkHttpClient.Builder();
		}

		if (socketFactory != null) {
			builder.socketFactory(socketFactory);
		}

		if (hostnameVerifier != null) {
			builder.hostnameVerifier(hostnameVerifier);
		}

		if (sslSocketFactory != null) {
			builder.sslSocketFactory(sslSocketFactory, trustManager);
		}

		if (callTimeout != null) {
			builder.callTimeout(callTimeout);
		}

		if (connectTimeout != null) {
			builder.connectTimeout(connectTimeout);
		}

		if (readTimeout != null) {
			builder.readTimeout(readTimeout);
		}

		if (writeTimeout != null) {
			builder.writeTimeout(writeTimeout);
		}

		if (cookieJar != null) {
			builder.cookieJar(cookieJar);
		}

		return builder;
	}

	public OkHttpClient build() {
		return build(okHttpClientBuilder);
	}

	public OkHttpClient build(okhttp3.OkHttpClient.Builder clientBuilder) {
		return new OkHttpClient(builder(clientBuilder).build(), proxyPool);
	}

	public OkHttpClient build(UnaryOperator<okhttp3.OkHttpClient.Builder> operator) {
		okhttp3.OkHttpClient.Builder build = builder(okHttpClientBuilder);
		return new OkHttpClient(operator.apply(build).build(), proxyPool);
	}

	public OkHttpClientBuilder okHttpClientBuilder(okhttp3.OkHttpClient.Builder httpClientBuilder) {
		this.okHttpClientBuilder = httpClientBuilder;
		return this;
	}

	public OkHttpClientBuilder socketFactory(SocketFactory socketFactory) {
		this.socketFactory = socketFactory;
		return this;
	}

	public OkHttpClientBuilder hostnameVerifier(HostnameVerifier hostnameVerifier) {
		this.hostnameVerifier = hostnameVerifier;
		return this;
	}

	public OkHttpClientBuilder sslSocketFactory(SSLSocketFactory ssf) {
		this.sslSocketFactory = ssf;
		return this;
	}

	public OkHttpClientBuilder trustManager(X509TrustManager trustManager) {
		this.trustManager = trustManager;
		return this;
	}

	public OkHttpClientBuilder disableSsl() {
		return sslSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault())
			.trustManager(HttpsConstants.TRUST_MANAGER)
			.hostnameVerifier(HttpsConstants.HOSTNAME_VERIFIER);
	}

	public OkHttpClientBuilder callTimeout(Duration callTimeout) {
		this.callTimeout = callTimeout;
		return this;
	}

	public OkHttpClientBuilder connectTimeout(Duration connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public OkHttpClientBuilder readTimeout(Duration readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}

	public OkHttpClientBuilder writeTimeout(Duration writeTimeout) {
		this.writeTimeout = writeTimeout;
		return this;
	}

	/**
	 * 无限等待时间
	 */
	public OkHttpClientBuilder infiniteTimeout() {
		return timeout(Duration.ZERO, Duration.ZERO, Duration.ZERO, Duration.ZERO);
	}

	public OkHttpClientBuilder timeout(Duration connectTimeout, Duration readTimeout) {
		return connectTimeout(connectTimeout).readTimeout(readTimeout);
	}

	public OkHttpClientBuilder timeout(Duration callTimeout, Duration connectTimeout, Duration readTimeout,
									   Duration writeTimeout) {
		return callTimeout(callTimeout).connectTimeout(connectTimeout)
			.readTimeout(readTimeout)
			.writeTimeout(writeTimeout);
	}

	public OkHttpClientBuilder proxyPool(ProxyPool pool) {
		this.proxyPool = pool;
		return this;
	}

	public OkHttpClientBuilder cookieJar(CookieJar jar) {
		this.cookieJar = jar;
		return this;
	}

	public OkHttpClientBuilder keepCookieJar() {
		return cookieJar(new OkHttpKeepCookieJar());
	}

}
