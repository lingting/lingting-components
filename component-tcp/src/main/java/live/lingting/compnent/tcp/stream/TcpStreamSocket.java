package live.lingting.compnent.tcp.stream;

import live.lingting.component.core.util.StreamUtils;
import live.lingting.component.core.util.ValueUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author lingting 2024-01-16 14:29
 */
@Slf4j
public abstract class TcpStreamSocket {

	public final boolean isServer;

	protected final Socket socket;

	protected final InputStream input;

	protected final OutputStream output;

	protected TcpStreamSocket(boolean isServer, Socket socket) throws IOException {
		this.isServer = isServer;
		this.socket = socket;
		this.input = socket.getInputStream();
		this.output = socket.getOutputStream();
	}

	/**
	 * 此数据是否表示要断开当前连接
	 * @param bytes 数据
	 * @return true 表示要断开当前连接
	 */
	public abstract boolean isClosed(byte[] bytes);

	/**
	 * 此数据是否为完整数据
	 */
	public abstract boolean isComplete(byte[] bytes);

	/**
	 * 处理接收到的完整数据
	 * @param bytes 完整数据
	 * @return 返回要发送的数据
	 */
	public abstract InputStream input(byte[] bytes);

	public abstract void onConnected();

	public abstract void onException(Exception e);

	public abstract void onClose();

	public void start() {
		try {
			ValueUtils.awaitTrue(socket::isConnected);
			onConnected();
			read();
		}
		catch (IOException e) {
			onException(e);
		}
		finally {
			onClose();
		}
	}

	protected void read() throws IOException {
		int size = 1024 * 1024 * 10;
		byte[] bytes = new byte[size];
		ByteArrayOutputStream stream = new ByteArrayOutputStream(size);

		while (!socket.isClosed()) {
			int len = input.read(bytes);
			for (int i = 0; i < len; i++) {
				stream.write(bytes, i, 1);
				if (handlerStream(stream)) {
					StreamUtils.close(socket);
					return;
				}
			}
		}
	}

	private boolean handlerStream(ByteArrayOutputStream stream) {
		try {
			byte[] array = stream.toByteArray();
			// 关闭
			if (isClosed(array)) {
				return true;
			}
			else if (isComplete(array)) {
				InputStream data = input(array);
				write(data);
				stream.reset();
			}
		}
		catch (Exception e) {
			onException(e);
		}
		return false;
	}

	protected void write(InputStream data) throws IOException {
		StreamUtils.write(data, output);
		output.flush();
	}

}
