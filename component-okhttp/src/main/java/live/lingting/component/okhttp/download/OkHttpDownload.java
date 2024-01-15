package live.lingting.component.okhttp.download;

import live.lingting.component.core.util.ValueUtils;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.okhttp.exception.OkHttpDownloadException;
import lombok.Getter;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-12-20 16:43
 */
public abstract class OkHttpDownload {

	private final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected final OkHttpClient client;

	protected final ThreadPoolExecutor executor;

	protected final String url;

	protected final File dir;

	protected final String filename;

	@Getter
	protected boolean start = false;

	protected OkHttpDownloadException ex = null;

	protected OkHttpDownload(OkHttpClient client, ThreadPoolExecutor executor, String url, File dir, String filename) {
		this.client = client;
		this.executor = executor;
		this.url = url;
		this.dir = dir;
		this.filename = filename;
	}

	public static OkHttpDownloadBuilder builder(String url) {
		return new OkHttpDownloadBuilder(url);
	}

	public static OkHttpDownloadBuilder single(String url) {
		return new OkHttpDownloadBuilder(url).single();
	}

	public static OkHttpDownloadBuilder multi(String url) {
		return new OkHttpDownloadBuilder(url).multi();
	}

	/**
	 * 开始下载, 如果已开始或者已完成则不执行下载任务
	 */
	public synchronized OkHttpDownload start() throws IOException {
		if (isStart()) {
			return this;
		}
		start = true;

		if (!isFinished()) {
			String name = "DOWNLOAD-" + filename;
			executor.execute(() -> {
				Thread.currentThread().setName(name);
				try {
					doStart();
				}
				catch (Exception e) {
					log.error("下载异常!", e);
					ex = e instanceof OkHttpDownloadException ? (OkHttpDownloadException) e
							: new OkHttpDownloadException("下载异常!", e);
				}
			});
		}
		return this;
	}

	/**
	 * 执行下载任务
	 */
	protected abstract void doStart() throws IOException;

	/**
	 * 等待下载完成
	 */
	public OkHttpDownload await() throws InterruptedException {
		if (!isStart()) {
			throw new OkHttpDownloadException("download not start");
		}
		ValueUtils.await(this::isFinished, v -> v);
		return this;
	}

	public abstract boolean isFinished();

	/**
	 * 判断是否下载成功, 注意: 在未下载完成之前, 该方法均返回true
	 */
	public boolean isSuccess() {
		return ex == null;
	}

	/**
	 * 等待任务下载完成, 并且返回下载后的文件
	 */
	public File getFile() throws InterruptedException, OkHttpDownloadException {
		await();
		if (ex != null) {
			throw ex;
		}
		return target();
	}

	protected File target() {
		return new File(dir, filename);
	}

}
