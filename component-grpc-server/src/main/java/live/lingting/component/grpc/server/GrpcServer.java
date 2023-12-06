package live.lingting.component.grpc.server;

import io.grpc.Server;
import live.lingting.component.core.context.ContextComponent;
import live.lingting.component.core.util.ThreadUtils;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-04-14 17:38
 */
@Slf4j
@Getter
public class GrpcServer implements ContextComponent {

	private final Server server;

	public GrpcServer(Server server) {
		this.server = server;
	}

	public boolean isRunning() {
		return !server.isShutdown() && !server.isTerminated();
	}

	public int port() {
		return server.getPort();
	}

	@Override
	@SneakyThrows
	public void onApplicationStart() {
		server.start();
		log.debug("grpc服务启动. 端口: {}", server.getPort());
		ThreadUtils.execute("GrpcServer", server::awaitTermination);
	}

	@Override
	public void onApplicationStop() {
		log.warn("grpc服务开始关闭");
		server.shutdownNow();
		log.warn("grpc服务关闭");
	}

}
