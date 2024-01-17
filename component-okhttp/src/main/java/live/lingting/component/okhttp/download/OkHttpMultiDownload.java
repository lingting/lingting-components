package live.lingting.component.okhttp.download;

import live.lingting.component.core.download.AbstractMultiDownload;
import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.okhttp.OkHttpClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author lingting 2024-01-15 17:35
 */
@Slf4j
@Getter
public class OkHttpMultiDownload extends AbstractMultiDownload<OkHttpMultiDownload> implements OkHttpDownload {

	protected final OkHttpClient client;

	protected OkHttpMultiDownload(OkHttpDownloadBuilder builder) {
		super(builder);
		this.client = builder.client;
	}

	@Override
	protected long remoteSize() {
		try (Response response = client.get(url)) {
			ResponseBody body = getBody(response);
			return body.contentLength();
		}
	}

	@Override
	protected void write(OutputStream output, long start, long end) throws IOException {
		Request.Builder builder = new Request.Builder().url(url)
			.addHeader("Range", String.format("bytes=%d-%d", start, end));

		try (Response response = client.request(builder.build())) {
			ResponseBody body = getBody(response);
			InputStream input = body.byteStream();
			StreamUtils.write(input, output);
		}
	}

}
