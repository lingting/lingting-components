package live.lingting.component.tcp;

import live.lingting.compnent.tcp.TcpResponse;
import live.lingting.compnent.tcp.client.TcpClient;
import live.lingting.compnent.tcp.server.TcpServer;
import live.lingting.compnent.tcp.server.TcpServerBuilder;
import live.lingting.compnent.tcp.server.TcpServerOutputListener;
import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.thread.Async;
import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.core.util.RandomUtils;
import live.lingting.component.core.util.StreamUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author lingting 2023-09-25 17:15
 */
@Slf4j
@SuppressWarnings("java:S2925")
class TcpTest {

	static final String localhost = "127.0.0.1";

	static final String host = "0.0.0.0";

	static final int port = 9025;

	static final Charset charset = StandardCharsets.UTF_8;

	static final TcpClient client = new TcpClient(localhost, port).charset(charset);

	static TcpServer server;

	static Exception ex = null;

	@Test
	void test() throws IOException {
		Listener listener = new Listener();
		server = new TcpServerBuilder().address(host).port(port).listener(listener).build();
		server.start();
		assertFalse(server.isClosed());

		ThreadPool.instance().execute("Client", new Client());

		server.awaitTermination();
		assertNull(ex);
	}

	static class Listener implements TcpServerOutputListener {

		@Override
		public InputStream handler(Socket socket) throws Exception {
			InputStream input = socket.getInputStream();
			byte[] bytes = StreamUtils.read(input);

			String body = new String(bytes, charset);
			assertNotNull(body);
			log.debug("server recv: {}", body);
			int millis = RandomUtils.nextInt(1000, 5000);
			Thread.sleep(millis);
			String response = "response: " + body + "; " + millis;
			if (millis < 2500) {
				byte[] responseBytes = response.getBytes(charset);
				return new ByteArrayInputStream(responseBytes);
			}

			return null;
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

	static class Client implements ThrowingRunnable {

		@Override
		public void run() throws Exception {

			Async async = new Async();
			for (int i = 0; i < 10; i++) {
				String body = "send body: " + i;

				async.submit("tcp " + i, () -> {
					try (TcpResponse response = client.call(body)) {
						String string = response.string();
						assertNotNull(string);
						log.debug("client recv: {}", string);
					}
				});
			}

			async.await();
			server.close();
		}

	}

}
