package live.lingting.compnent.tcp.server;

import live.lingting.component.core.thread.ThreadPool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2024-01-16 13:38
 */
public class TcpServerBuilder {

	protected Integer port;

	protected int backlog = 50;

	protected InetAddress address;

	protected ThreadPoolExecutor executor = ThreadPool.instance().getPool();

	protected TcpServerListener listener;

	public TcpServerBuilder port(Integer port) {
		this.port = port;
		return this;
	}

	public TcpServerBuilder backlog(int backlog) {
		this.backlog = backlog;
		return this;
	}

	public TcpServerBuilder address(InetAddress address) {
		this.address = address;
		return this;
	}

	public TcpServerBuilder address(String address) throws UnknownHostException {
		return address(InetAddress.getByName(address));
	}

	public TcpServerBuilder executor(ThreadPoolExecutor executor) {
		this.executor = executor;
		return this;
	}

	public TcpServerBuilder listener(TcpServerListener listener) {
		this.listener = listener;
		return this;
	}

	public TcpServer build() {
		return new TcpServer(this);
	}

}
