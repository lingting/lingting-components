package live.lingting.compnent.tcp.server;

import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.core.util.ValueUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-09-25 16:49
 */
@Slf4j
public class TcpServer implements Closeable {

	@Getter
	protected final Integer port;

	@Getter
	protected final int backlog;

	@Getter
	protected final InetAddress address;

	protected final ThreadPoolExecutor executor;

	protected final TcpServerListener listener;

	protected ServerSocket server;

	public TcpServer(TcpServerBuilder builder) {
		if (builder.port == null || builder.port < 0 || builder.port > 0xFFFF) {
			throw new IllegalArgumentException("port out of range: " + builder.port);
		}

		if (builder.address == null) {
			throw new IllegalArgumentException("address not valid!");
		}

		if (builder.executor == null) {
			throw new IllegalArgumentException("thread pool is null!");
		}

		if (builder.listener == null) {
			throw new IllegalArgumentException("server listener is null!");
		}

		this.port = builder.port;
		this.backlog = builder.backlog;
		this.address = builder.address;
		this.executor = builder.executor;
		this.listener = builder.listener;
	}

	public synchronized void start() throws IOException {
		if (server != null) {
			return;
		}
		server = new ServerSocket(port, backlog, address);
		executor.execute(() -> {
			Thread thread = Thread.currentThread();
			thread.setName("TcpServerAccept");
			try {
				accept();
			}
			finally {
				listener.onClose();
			}
		});
	}

	protected void accept() {
		while (!server.isClosed()) {
			Socket socket = awaitSocket();
			if (socket != null) {
				executor.execute(() -> onAccept(socket));
			}
		}
	}

	protected Socket awaitSocket() {
		while (!server.isClosed()) {
			try {
				return server.accept();
			}
			catch (Exception e) {
				if (e instanceof SocketException && server.isClosed()) {
					// 服务关闭异常, 直接忽略
				}
				else {
					listener.onAcceptException(e);
				}
			}
		}
		return null;
	}

	protected void onAccept(Socket socket) {
		try {
			listener.onAccept(socket);
		}
		catch (Exception e) {
			listener.onException(socket, e);
		}
		finally {
			listener.onFinally(socket);
		}
	}

	public void awaitTermination() {
		Thread thread = Thread.currentThread();
		ValueUtils.awaitTrue(() -> thread.isInterrupted() || server.isClosed());
		// 关闭服务
		close();
	}

	public boolean isClosed() {
		return server.isClosed();
	}

	@Override
	public void close() {
		StreamUtils.close(server);
	}

}
