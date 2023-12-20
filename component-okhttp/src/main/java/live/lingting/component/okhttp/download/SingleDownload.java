package live.lingting.component.okhttp.download;

import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.core.util.ThreadUtils;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.okhttp.exception.OkHttpDownloadException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.Duration;

/**
 * @author lingting 2023-12-20 17:01
 */
@Slf4j
public class SingleDownload extends OkHttpDownload {

	private static final OkHttpClient CLIENT = OkHttpClient.builder()
		.disableSsl()
		.callTimeout(Duration.ofSeconds(10))
		.connectTimeout(Duration.ofSeconds(10))
		.readTimeout(Duration.ofSeconds(10))
		.build();

	protected boolean finished = false;

	protected SingleDownload(String url, File dir, String filename) {
		super(url, dir, filename);
	}

	@Override
	protected void doStart() throws IOException {
		ThreadUtils.execute("DOWNLOAD-" + filename, () -> {
			try (Response response = CLIENT.get(url)) {
				ResponseBody body = response.body();
				if (body == null) {
					throw new OkHttpDownloadException("download body is null!");
				}

				File target = target();

				if (!FileUtils.createFile(target)) {
					throw new OkHttpDownloadException("file create failed! file: " + target.getAbsolutePath());
				}

				InputStream stream = body.byteStream();
				OutputStream out = Files.newOutputStream(target.toPath());
				// 写入流到文件
				StreamUtils.write(stream, out, (int) DataSize.ofMegabytes(10).toBytes());
				// 关闭流
				StreamUtils.close(out);
			}
			catch (OkHttpDownloadException e) {
				ex = e;
			}
			catch (IOException e) {
				ex = new OkHttpDownloadException("文件下载异常! ", e);
			}
			finally {
				finished = true;
			}

		});
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

}
