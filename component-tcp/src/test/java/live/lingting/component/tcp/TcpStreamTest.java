package live.lingting.component.tcp;

import live.lingting.compnent.tcp.client.TcpClient;
import live.lingting.compnent.tcp.server.TcpServer;
import live.lingting.compnent.tcp.server.TcpServerBuilder;
import live.lingting.compnent.tcp.stream.TcpServerStreamListener;
import live.lingting.compnent.tcp.stream.TcpStream;
import live.lingting.compnent.tcp.stream.TcpStreamSocket;
import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.core.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author lingting 2024-01-16 14:58
 */
@Slf4j
class TcpStreamTest {

	static final String localhost = "127.0.0.1";

	static final String host = "0.0.0.0";

	static final int port = 9025;

	static final Charset charset = StandardCharsets.UTF_8;

	static final TcpClient client = new TcpClient(localhost, port).charset(charset);

	static TcpServer server;

	static Exception ex = null;

	@Test
	void test() throws IOException {
		Stream stream = new Stream();
		Listener listener = new Listener(stream);
		server = new TcpServerBuilder().address(host).port(port).listener(listener).build();
		server.start();
		assertFalse(server.isClosed());

		ThreadPool.instance().execute("Client", new Client(stream));

		server.awaitTermination();
		assertNull(ex);
	}

	static class Stream implements TcpStream {

		@SneakyThrows
		@Override
		public TcpStreamSocket useClient(Socket socket) {
			return new TcpSocket(false, socket);
		}

		@SneakyThrows
		@Override
		public TcpStreamSocket useServer(Socket socket) {
			return new TcpSocket(true, socket);
		}

	}

	static class TcpSocket extends TcpStreamSocket {

		AtomicLong atomic = new AtomicLong(0);

		public TcpSocket(boolean server, Socket socket) throws IOException {
			super(server, socket);
		}

		@Override
		public boolean isClosed(byte[] bytes) {
			return "close".equalsIgnoreCase(new String(bytes, charset));
		}

		@Override
		public boolean isComplete(byte[] bytes) {
			return bytes.length == 10;
		}

		@Override
		public InputStream input(byte[] bytes) {
			byte[] data = bytes;
			long get = atomic.incrementAndGet();
			if (get > 10) {
				data = "close".getBytes(charset);
			}
			log.debug("server: {}; recv: {}; send: {}; i: {}", isServer, StringUtils.hex(bytes), StringUtils.hex(data),
					get);
			return new ByteArrayInputStream(data);
		}

		@SneakyThrows
		@Override
		public void onConnected() {
			log.debug("onConnected");
			write(new ByteArrayInputStream(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
		}

		@Override
		public void onException(Exception e) {
			log.error("onException", e);
			ex = e;
		}

		@Override
		public void onClose() {
			log.warn("onClose");
			server.close();
		}

	}

	static class Listener extends TcpServerStreamListener {

		public Listener(TcpStream stream) {
			super(stream);
		}

		@Override
		public void onException(Socket socket, Exception e) {
			ex = e;
			log.error("onException! socket: {}", socket, e);
		}

		@Override
		public void onFinally(Socket socket) {
			log.debug("onFinally: {}", socket);
		}

		@Override
		public void onAcceptException(Exception e) {
			ex = e;
			log.error("onAcceptException!", e);
		}

		@Override
		public void onClose() {
			log.warn("onClose");
		}

	}

	@RequiredArgsConstructor
	static class Client implements ThrowingRunnable {

		protected final TcpStream stream;

		@Override
		public void run() throws Exception {
			client.stream(stream);
		}

	}

}
