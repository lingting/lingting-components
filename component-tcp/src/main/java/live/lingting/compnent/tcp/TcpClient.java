package live.lingting.compnent.tcp;

import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author lingting 2023-09-25 16:26
 */
@RequiredArgsConstructor
public class TcpClient {

	private final String host;

	private final int port;

	private Charset charset = StandardCharsets.UTF_8;

	public TcpClient charset(Charset charset) {
		this.charset = charset;
		return this;
	}

	public TcpResponse call(byte[] bytes) throws IOException {
		Socket socket = new Socket(host, port);
		OutputStream output = socket.getOutputStream();
		output.write(bytes);
		output.flush();
		socket.shutdownOutput();
		return new TcpResponse(socket);
	}

	public TcpResponse call(String string) throws IOException {
		return call(string, charset);
	}

	public TcpResponse call(String string, Charset charset) throws IOException {
		return call(string.getBytes(charset));
	}

}
