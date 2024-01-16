package live.lingting.compnent.tcp.server;

import java.net.Socket;

/**
 * @author lingting 2024-01-16 13:21
 */
@SuppressWarnings("java:S112")
public interface TcpServerListener {

	/**
	 * 收到会话
	 */
	void onAccept(Socket socket) throws Exception;

	/**
	 * 会话异常或者会话处理异常
	 */
	void onException(Socket socket, Exception e);

	/**
	 * 会话处理完成
	 */
	void onFinally(Socket socket);

	/**
	 * 服务等待连接时异常
	 */
	void onAcceptException(Exception e);

	/**
	 * 服务关闭
	 */
	void onClose();

}
