package live.lingting.component.okhttp.download;

import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.okhttp.OkHttpClient;
import live.lingting.component.okhttp.exception.OkHttpDownloadException;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-12-20 17:01
 */
public class SingleDownload extends OkHttpDownload {

	protected boolean finished = false;

	public SingleDownload(OkHttpClient client, ThreadPoolExecutor executor, String url, File dir, String filename) {
		super(client, executor, url, dir, filename);
	}

	@Override
	protected void doStart() {
		try (Response response = client.get(url)) {
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
		catch (IOException e) {
			throw new OkHttpDownloadException("file download io exception! ", e);
		}
		finally {
			finished = true;
		}
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

}
