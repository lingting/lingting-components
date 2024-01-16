package live.lingting.compnent.tcp.stream;

import live.lingting.component.core.thread.ThreadPool;

import java.net.Socket;

/**
 * 双向流
 *
 * @author lingting 2024-01-16 14:25
 */
public interface TcpStream {

	TcpStreamSocket useClient(Socket socket);

	TcpStreamSocket useServer(Socket socket);

	default void async(TcpStreamSocket socket) {
		ThreadPool pool = ThreadPool.instance();
		pool.execute(socket::start);
	}

}
