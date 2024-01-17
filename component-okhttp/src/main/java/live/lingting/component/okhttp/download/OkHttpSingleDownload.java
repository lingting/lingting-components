package live.lingting.component.okhttp.download;

import live.lingting.component.core.download.AbstractSingleDownload;
import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.okhttp.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.util.unit.DataSize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author lingting 2023-12-20 17:01
 */
public class OkHttpSingleDownload extends AbstractSingleDownload<OkHttpSingleDownload> implements OkHttpDownload {

	protected final OkHttpClient client;

	protected OkHttpSingleDownload(OkHttpDownloadBuilder builder) {
		super(builder);
		this.client = builder.client;
	}

	@Override
	protected void write(OutputStream out) throws IOException {
		try (Response response = client.get(url)) {
			ResponseBody body = getBody(response);

			InputStream stream = body.byteStream();
			// 写入流到文件
			StreamUtils.write(stream, out, (int) DataSize.ofMegabytes(10).toBytes());
		}
	}

}
