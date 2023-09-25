package live.lingting.component.grpc;

import io.grpc.Server;
import live.lingting.component.core.ContextComponent;
import live.lingting.component.core.thread.ThreadPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-04-14 17:38
 */
@Slf4j
public class GrpcServer implements ContextComponent {

	private static final ThreadPool THREAD_POOL = ThreadPool.instance();

	private final Server server;

	public GrpcServer(Server server) {
		this.server = server;
	}

	@Override
	@SneakyThrows
	public void onApplicationStart() {
		server.start();
		log.debug("grpc服务启动. 端口: {}", server.getPort());
		THREAD_POOL.execute("GrpcServer", server::awaitTermination);
	}

	@Override
	public void onApplicationStop() {
		log.warn("grpc服务开始关闭");
		server.shutdownNow();
		log.warn("grpc服务关闭");
	}

}
