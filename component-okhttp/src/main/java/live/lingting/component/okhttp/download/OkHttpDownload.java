package live.lingting.component.okhttp.download;

import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.ValueUtils;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.okhttp.exception.OkHttpDownloadException;
import lombok.Getter;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Consumer;

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

	protected OkHttpDownload(OkHttpDownloadBuilder builder) {
		this.client = builder.client;
		this.executor = builder.executor;
		this.url = builder.url;
		this.dir = builder.dir;
		this.filename = builder.filename;
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
			async(name, this::doStart, e -> {
				log.error("下载异常!", e);
				upsertEx(e);
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
		ValueUtils.awaitTrue(this::isFinished);
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

	protected File createTarget() {
		File target = target();

		if (!FileUtils.createFile(target)) {
			throw new OkHttpDownloadException("file create failed! file: " + target.getAbsolutePath());
		}
		return target;
	}

	protected ResponseBody getBody(Response response) {
		if (!response.isSuccessful()) {
			throw new OkHttpDownloadException(String.format("response status: %d", response.code()));
		}
		ResponseBody body = response.body();
		if (body == null) {
			throw new OkHttpDownloadException("download body is null!");
		}
		return body;
	}

	protected void async(String threadName, ThrowingRunnable runnable, Consumer<Exception> onException) {
		executor.execute(() -> {
			Thread thread = Thread.currentThread();
			String oldName = thread.getName();
			thread.setName(threadName);
			try {
				runnable.run();
			}
			catch (Exception e) {
				onException.accept(e);
			}
			finally {
				thread.setName(oldName);
			}
		});
	}

	protected void upsertEx(Exception e) {
		ex = e instanceof OkHttpDownloadException ? (OkHttpDownloadException) e
				: new OkHttpDownloadException("下载异常!", e);
	}

}
