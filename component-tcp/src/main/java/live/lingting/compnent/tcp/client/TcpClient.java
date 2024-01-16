package live.lingting.compnent.tcp.client;

import live.lingting.compnent.tcp.TcpResponse;
import live.lingting.compnent.tcp.stream.TcpStream;
import live.lingting.compnent.tcp.stream.TcpStreamSocket;
import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.core.util.StreamUtils;
import lombok.RequiredArgsConstructor;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-09-25 16:26
 */
@RequiredArgsConstructor
public class TcpClient {

	protected final String host;

	protected final int port;

	protected Charset charset = StandardCharsets.UTF_8;

	protected ThreadPoolExecutor executor = ThreadPool.instance().getPool();

	public TcpClient charset(Charset charset) {
		this.charset = charset;
		return this;
	}

	public TcpClient executor(ThreadPoolExecutor executor) {
		this.executor = executor;
		return this;
	}

	public void stream(TcpStream stream) throws IOException {
		Socket socket = new Socket(host, port);
		TcpStreamSocket client = stream.useClient(socket);
		stream.async(client);
	}

	@SuppressWarnings("java:S2095")
	public TcpResponse call(InputStream input) throws IOException {
		Socket socket = new Socket(host, port);
		OutputStream output = socket.getOutputStream();
		StreamUtils.write(input, output);
		output.flush();
		socket.shutdownOutput();
		return new TcpResponse(socket);
	}

	public TcpResponse call(byte[] bytes) throws IOException {
		ByteArrayInputStream input = new ByteArrayInputStream(bytes);
		return call(input);
	}

	public TcpResponse call(String string) throws IOException {
		return call(string, charset);
	}

	public TcpResponse call(String string, Charset charset) throws IOException {
		return call(string.getBytes(charset));
	}

}
