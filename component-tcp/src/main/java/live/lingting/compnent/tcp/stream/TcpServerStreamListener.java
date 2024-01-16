package live.lingting.compnent.tcp.stream;

import live.lingting.compnent.tcp.server.TcpServerListener;
import lombok.RequiredArgsConstructor;

import java.net.Socket;

/**
 * @author lingting 2024-01-16 15:06
 */
@RequiredArgsConstructor
public abstract class TcpServerStreamListener implements TcpServerListener {

	protected final TcpStream stream;

	@Override
	public void onAccept(Socket socket) throws Exception {
		TcpStreamSocket server = stream.useServer(socket);
		stream.async(server);
	}

}
