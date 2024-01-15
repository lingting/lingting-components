package live.lingting.component.okhttp.download;

import live.lingting.component.core.constant.HttpConstants;
import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.SystemUtils;
import live.lingting.component.core.util.ThreadUtils;
import live.lingting.component.okhttp.OkHttpClient;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-12-20 16:49
 */
public class OkHttpDownloadBuilder {

	private static final OkHttpClient CLIENT_DEFAULT = OkHttpClient.builder()
		.disableSsl()
		.callTimeout(Duration.ofSeconds(10))
		.connectTimeout(Duration.ofSeconds(10))
		.readTimeout(Duration.ofSeconds(10))
		.build();

	/**
	 * 文件下载地址
	 */
	private final String url;

	/**
	 * 文件存放文件夹
	 */
	private File dir = new File(SystemUtils.tmpDirLingting(), "download");

	/**
	 * 为空从url解析
	 */
	private String filename;

	/**
	 * 客户端配置
	 */
	private OkHttpClient client = CLIENT_DEFAULT;

	private ThreadPoolExecutor executor = ThreadUtils.instance().getPool();

	private boolean multi = false;

	public OkHttpDownloadBuilder(String url) {
		String[] split = url.split(HttpConstants.HOST_DELIMITER);

		this.url = url;
		this.filename = split[split.length - 1];
	}

	public OkHttpDownloadBuilder dir(File dir) {
		FileUtils.createDir(dir);
		this.dir = dir;
		return this;
	}

	public OkHttpDownloadBuilder filename(String filename) {
		this.filename = filename;
		return this;
	}

	public OkHttpDownloadBuilder client(OkHttpClient client) {
		this.client = client;
		return this;
	}

	public OkHttpDownloadBuilder executor(ThreadPoolExecutor executor) {
		this.executor = executor;
		return this;
	}

	public OkHttpDownloadBuilder single() {
		this.multi = false;
		return this;
	}

	public OkHttpDownloadBuilder multi() {
		this.multi = true;
		return this;
	}

	public OkHttpDownload build() {
		return new SingleDownload(client, executor, url, dir, filename);
	}

}
