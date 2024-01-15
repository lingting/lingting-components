package live.lingting.component.okhttp.download;

import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.core.util.ValueUtils;
import live.lingting.component.core.value.StepValue;
import live.lingting.component.core.value.step.ConcurrentStepValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2024-01-15 17:35
 */
@Slf4j
@Getter
public class MultiDownload extends OkHttpDownload {

	/**
	 * 最大启动线程数
	 */
	protected final int maxThreadCount;

	/**
	 * 每个分片的最大大小, 单位: bytes
	 */
	protected final long maxShardSize;

	/**
	 * 文件大小, 用于多线程下载时进行分片. 单位: bytes
	 * <p>
	 * 设置为null或者小于1时自动解析文件具体大小
	 * </p>
	 */
	protected Long fileSize;

	protected boolean finished = false;

	protected long maxShard;

	protected long finishedShard;

	protected MultiDownload(OkHttpDownloadBuilder builder) {
		super(builder);
		this.fileSize = builder.fileSize;
		this.maxThreadCount = builder.maxThreadCount;
		this.maxShardSize = builder.maxShardSize;
	}

	@Override
	protected void doStart() throws IOException {
		// 文件大小
		long size = generateFileSize();
		// 创建文件
		File target = createTarget();
		// 创建为一个随机访问文件, 并且设置大小
		try (RandomAccessFile file = new RandomAccessFile(target, "rw")) {
			StreamUtils.close(file);
		}

		// 最大分片数
		maxShard = size % maxShardSize == 0 ? size / maxShardSize : size / maxShardSize + 1;

		StepValue<Long> step = new ConcurrentStepValue<>(0L, (c, p) -> {
			if (c == 0) {
				return p;
			}
			long n = p + maxShardSize;
			if (n >= size) {
				return null;
			}
			return n;
		});

		List<MultiDownloadTask> tasks = new ArrayList<>();

		for (int i = 0; i < maxThreadCount; i++) {
			final int index = i;
			MultiDownloadTask task = new MultiDownloadTask(this, target, step);
			async(String.format("DOWNLOAD-%d-%s", index, filename), task::start, e -> {
				log.error("下载异常! 线程序号: {}", index, e);
				task.stop();
			});
			tasks.add(task);
		}

		ValueUtils.await(() -> {
			long finishedTask = 0;
			long currentFinishedShard = 0;

			for (MultiDownloadTask task : tasks) {
				if (task.isFinished()) {
					// 更新已完成任务数
					finishedTask++;
				}
				// 如果发生异常但是还是运行中, 结束
				else if (ex != null) {
					task.stop();
				}
				// 更新已下载完成分片数
				currentFinishedShard += task.getCount();
				// 如果发生异常, 中断
			}
			finishedShard = currentFinishedShard;

			return tasks.size() - finishedTask;
		}, i -> i < 1);
		finished = true;
	}

	protected long generateFileSize() {
		if (fileSize != null) {
			return fileSize;
		}

		try (Response response = client.get(url)) {
			ResponseBody body = getBody(response);
			fileSize = body.contentLength();
		}
		return fileSize;
	}

}
