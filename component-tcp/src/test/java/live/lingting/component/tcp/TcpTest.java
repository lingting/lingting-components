package live.lingting.component.tcp;

import live.lingting.compnent.tcp.TcpClient;
import live.lingting.compnent.tcp.TcpResponse;
import live.lingting.compnent.tcp.TcpServer;
import live.lingting.component.core.Async;
import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.core.util.RandomUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

/**
 * @author lingting 2023-09-25 17:15
 */
@Slf4j
class TcpTest {

	String host = "127.0.0.1";

	int port = 9025;

	Charset charset = StandardCharsets.UTF_8;

	TcpClient client = new TcpClient(host, port).charset(charset);

	@Test
	void server() throws IOException {
		try (TcpServer server = new TcpServer(host, port)) {
			server.open().handler(new Function<byte[], byte[]>() {
				@SneakyThrows
				@Override
				public byte[] apply(byte[] bytes) {
					String body = new String(bytes, charset);
					System.out.println("recv: " + body);
					String response = "response: " + body;
					Thread.sleep(RandomUtils.nextInt(1000, 5000));
					return response.getBytes(charset);
				}
			}).awaitTermination();
		}
	}

	@Test
	void client() throws IOException, InterruptedException {
		ThreadPool pool = ThreadPool.instance();
		Async async = new Async();
		for (int i = 0; i < 10; i++) {
			String body = "send body: " + i;
			System.out.println("send: " + body);

			async.submit("tcp " + i, () -> {
				try (TcpResponse response = client.call(body)) {
					String string = response.string();
					System.out.println("recv: " + string);
				}
			});
		}

		async.await();
	}

}
