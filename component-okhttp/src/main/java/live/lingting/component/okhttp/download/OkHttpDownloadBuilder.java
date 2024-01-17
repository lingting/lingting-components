package live.lingting.component.okhttp.download;

import live.lingting.component.core.download.AbstractDownloadBuilder;
import live.lingting.component.okhttp.OkHttpClient;

import java.time.Duration;

/**
 * @author lingting 2023-12-20 16:49
 */
public class OkHttpDownloadBuilder extends AbstractDownloadBuilder<OkHttpDownloadBuilder> {

	static final OkHttpClient DEFAULT_CLIENT = OkHttpClient.builder()
		.disableSsl()
		.callTimeout(Duration.ofSeconds(10))
		.connectTimeout(Duration.ofSeconds(10))
		.readTimeout(Duration.ofSeconds(10))
		.build();

	/**
	 * 客户端配置
	 */
	OkHttpClient client = DEFAULT_CLIENT;

	protected OkHttpDownloadBuilder(String url) {
		super(url);
	}

	public OkHttpDownloadBuilder client(OkHttpClient client) {
		this.client = client;
		return this;
	}

	public OkHttpDownload build() {
		return multi ? new OkHttpMultiDownload(this) : new OkHttpSingleDownload(this);
	}

}
