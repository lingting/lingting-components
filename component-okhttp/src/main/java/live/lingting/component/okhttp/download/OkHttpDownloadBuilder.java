package live.lingting.component.okhttp.download;

import live.lingting.component.core.constant.HttpConstants;
import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.SystemUtils;
import live.lingting.component.core.util.ThreadUtils;
import live.lingting.component.okhttp.OkHttpClient;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-12-20 16:49
 */
public class OkHttpDownloadBuilder {

	static final OkHttpClient DEFAULT_CLIENT = OkHttpClient.builder()
		.disableSsl()
		.callTimeout(Duration.ofSeconds(10))
		.connectTimeout(Duration.ofSeconds(10))
		.readTimeout(Duration.ofSeconds(10))
		.build();

	static final int DEFAULT_MAX_THREAD_COUNT = 10;

	static final long DEFAULT_MAX_SHARD_SIZE = DataSize.ofMegabytes(10).toBytes();

	/**
	 * 文件下载地址
	 */
	final String url;

	/**
	 * 文件存放文件夹
	 */
	File dir = new File(SystemUtils.tmpDirLingting(), "download");

	/**
	 * 为空从url解析
	 */
	String filename;

	/**
	 * 客户端配置
	 */
	OkHttpClient client = DEFAULT_CLIENT;

	ThreadPoolExecutor executor = ThreadUtils.instance().getPool();

	boolean multi = false;

	/**
	 * 文件大小, 用于多线程下载时进行分片. 单位: bytes
	 * <p>
	 * 设置为null或者小于1时自动解析文件具体大小
	 * </p>
	 */
	Long fileSize;

	/**
	 * 最大启动线程数
	 */
	int maxThreadCount = DEFAULT_MAX_THREAD_COUNT;

	/**
	 * 每个分片的最大大小, 单位: bytes
	 */
	long maxShardSize = DEFAULT_MAX_SHARD_SIZE;

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

	public OkHttpDownloadBuilder fileSize(Long fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	public OkHttpDownloadBuilder maxThreadCount(int maxThreadCount) {
		this.maxThreadCount = safeDefault(maxThreadCount, DEFAULT_MAX_THREAD_COUNT);
		return this;
	}

	public OkHttpDownloadBuilder maxShardSize(long maxShardSize) {
		this.maxShardSize = safeDefault(maxShardSize, DEFAULT_MAX_SHARD_SIZE);
		return this;
	}

	public OkHttpDownload build() {
		return multi ? new MultiDownload(this) : new SingleDownload(this);
	}

	/**
	 * 将原值进行安装判断, 如果不满足则设置为默认值
	 * @param t 原值
	 * @param d 默认值
	 * @return 结果
	 */
	protected <T extends Number> T safeDefault(T t, T d) {
		if (t == null || t.longValue() < 1) {
			return d;
		}
		return t;
	}

}
