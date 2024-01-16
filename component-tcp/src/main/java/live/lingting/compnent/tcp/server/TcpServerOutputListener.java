package live.lingting.compnent.tcp.server;

import live.lingting.component.core.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author lingting 2024-01-16 13:29
 */
@SuppressWarnings("java:S112")
public interface TcpServerOutputListener extends TcpServerListener {

	@Override
	default void onAccept(Socket socket) throws Exception {
		try (InputStream input = handler(socket)) {
			if (input == null) {
				return;
			}

			write(input, socket.getOutputStream());
		}
		finally {
			socket.shutdownOutput();
		}
	}

	/**
	 * 处理连接, 并且返回数据
	 * @param socket 连接
	 * @return 返回值流
	 * @throws Exception 异常
	 */
	InputStream handler(Socket socket) throws Exception;

	default void write(InputStream input, OutputStream output) throws IOException {
		StreamUtils.write(input, output);
	}

}
