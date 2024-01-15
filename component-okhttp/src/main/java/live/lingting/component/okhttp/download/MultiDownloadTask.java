package live.lingting.component.okhttp.download;

import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.core.value.StepValue;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.okhttp.exception.OkHttpDownloadException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.NoSuchElementException;

@Getter
@RequiredArgsConstructor
public class MultiDownloadTask {

	protected final MultiDownload download;

	protected final File target;

	protected final StepValue<Long> step;

	protected long count = 0;

	protected boolean finished = false;

	public void start() {
		Long size = download.getFileSize();
		long maxShardSize = download.getMaxShardSize();
		while (!finished) {
			Long next = next();
			if (next == null || next < 0 || next >= size) {
				stop();
				return;
			}

			// 起始值
			long start = next;
			// 结束值
			long end = Math.min(start + maxShardSize, size);
			tryDownload(start, end);
			count++;
		}
	}

	protected void tryDownload(long start, long end) {
		try {
			doDownload(start, end);
		}
		catch (Exception e) {
			throw new OkHttpDownloadException(
					String.format("multi download error[%d-%d:%s]!", start, end, download.getFileSize()), e);
		}
	}

	protected void doDownload(long start, long end) throws IOException {
		OkHttpClient client = download.client;
		Request.Builder builder = new Request.Builder().url(download.url)
			.addHeader("Range", String.format("bytes=%d-%d", start, end));

		try (Response response = client.request(builder.build())) {
			ResponseBody body = download.getBody(response);
			write(body.byteStream(), start);
		}
	}

	protected void write(InputStream stream, long start) throws IOException {
		int size = (int) DataSize.ofMegabytes(10).toBytes();
		try (RandomAccessFile file = new RandomAccessFile(target, "rw")) {
			file.seek(start);
			StreamUtils.read(stream, size, (length, bytes) -> file.write(bytes, 0, length));
		}
	}

	protected Long next() {
		try {
			return step.next();
		}
		catch (NoSuchElementException e) {
			return null;
		}
	}

	public void stop() {
		finished = true;
	}

}
