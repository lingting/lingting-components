package live.lingting.compnent.tcp;

import live.lingting.component.core.util.StreamUtils;
import lombok.Setter;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author lingting 2023-09-25 16:28
 */
public class TcpResponse implements Closeable {

	private final Socket socket;

	@Setter
	private Charset charset = StandardCharsets.UTF_8;

	TcpResponse(Socket socket) throws IOException {
		this.socket = socket;
	}

	// region 返回数据

	public byte[] bytes() throws IOException {
		InputStream input = socket.getInputStream();
		byte[] bytes = StreamUtils.read(input);
		return bytes;
	}

	public String string() throws IOException {
		byte[] bytes = bytes();
		return new String(bytes, charset);
	}

	// endregion

	@Override
	public void close() throws IOException {
		socket.close();
	}

}
