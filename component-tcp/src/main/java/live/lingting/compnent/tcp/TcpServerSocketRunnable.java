package live.lingting.compnent.tcp;

import live.lingting.component.core.function.ThrowingRunnable;
import live.lingting.component.core.util.StreamUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.function.Function;

/**
 * @author lingting 2023-09-25 19:25
 */
@Slf4j
@RequiredArgsConstructor
public class TcpServerSocketRunnable implements ThrowingRunnable {

	private final Socket socket;

	private final Function<byte[], byte[]> handler;

	@Override
	public void run() throws Exception {
		try {
			handler();
		}
		finally {
			StreamUtils.close(socket);
		}
	}

	void handler() throws IOException {
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
