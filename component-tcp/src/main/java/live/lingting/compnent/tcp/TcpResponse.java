package live.lingting.compnent.tcp;

import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.StreamUtils;
import lombok.Setter;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author lingting 2023-09-25 16:28
 */
public class TcpResponse implements Closeable {

	private final Socket socket;

	@Setter
	private Charset charset = StandardCharsets.UTF_8;

	private File file;

	TcpResponse(Socket socket) {
		this.socket = socket;
	}

	// region 返回数据

	public InputStream stream() throws IOException {
		if (file == null) {
			file = FileUtils.createTemp(".tcp");
			InputStream input = socket.getInputStream();
			OutputStream output = Files.newOutputStream(file.toPath());
			StreamUtils.write(input, output);
			StreamUtils.close(output);
		}
		return Files.newInputStream(file.toPath());
	}

	public byte[] bytes() throws IOException {
		InputStream input = stream();
		return StreamUtils.read(input);
	}

	public String string() throws IOException {
		byte[] bytes = bytes();
		if (bytes == null || bytes.length == 0) {
			return "";
		}
		return new String(bytes, charset);
	}

	// endregion

	@Override
	public void close() throws IOException {
		FileUtils.delete(file);
		socket.close();
	}

}
