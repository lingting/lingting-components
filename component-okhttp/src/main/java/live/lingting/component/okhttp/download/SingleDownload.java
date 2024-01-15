package live.lingting.component.okhttp.download;

import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.okhttp.exception.OkHttpDownloadException;
import lombok.Getter;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.util.unit.DataSize;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * @author lingting 2023-12-20 17:01
 */
@Getter
public class SingleDownload extends OkHttpDownload {

	protected boolean finished = false;

	protected SingleDownload(OkHttpDownloadBuilder builder) {
		super(builder);
	}

	@Override
	protected void doStart() {
		File target = createTarget();
		try (Response response = client.get(url)) {
			ResponseBody body = getBody(response);

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

}
