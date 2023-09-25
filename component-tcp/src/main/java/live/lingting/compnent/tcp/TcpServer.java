package live.lingting.compnent.tcp;

import live.lingting.component.core.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Function;

/**
 * @author lingting 2023-09-25 16:49
 */
@Slf4j
public class TcpServer implements Closeable {

	private final int port;

	private final int backlog;

	private final InetAddress address;

	private ServerSocket server;

	// 用于处理请求
	private Function<byte[], byte[]> handler;

	public TcpServer(int port) {
		this(null, port, 0);
	}

	public TcpServer(String host, int port) throws UnknownHostException {
		this(InetAddress.getByName(host), port);
	}

	public TcpServer(InetAddress address, int port) {
		this(address, port, 0);
	}

	public TcpServer(InetAddress address, int port, int backlog) {
		this.address = address;
		this.port = port;
		this.backlog = backlog;
	}

	public TcpServer open() throws IOException {
		if (server == null) {
			server = new ServerSocket(port, backlog, address);
		}
		return this;
	}

	public TcpServer handler(Function<byte[], byte[]> handler) {
		this.handler = handler;
		return this;
	}

	public void awaitTermination() throws IOException {
		Thread thread = Thread.currentThread();
		while (!thread.isInterrupted()) {
			try (Socket socket = server.accept()) {
				// 没有请求处理者
				if (handler == null) {
					log.warn("no tcp socket handler!");
					continue;
				}
				// 获取参数
				InputStream input = socket.getInputStream();
				byte[] read = StreamUtils.read(input);
				// 处理请求
				byte[] bytes = handler.apply(read);
				// 处理返回值
				if (bytes != null && bytes.length > 0) {
					OutputStream output = socket.getOutputStream();
					output.write(bytes);
				}
				socket.shutdownOutput();
			}
		}
	}

	@Override
	public void close() throws IOException {
		if (server != null) {
			server.close();
		}
	}

}
