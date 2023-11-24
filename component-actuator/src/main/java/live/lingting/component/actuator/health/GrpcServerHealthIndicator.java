package live.lingting.component.actuator.health;

import live.lingting.component.grpc.server.GrpcServer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

/**
 * @author lingting 2023-11-23 21:29
 */
@RequiredArgsConstructor
public class GrpcServerHealthIndicator extends AbstractHealthIndicator {

	private final GrpcServer server;

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		builder.status(server.isRunning() ? Status.UP : Status.DOWN)
			.withDetail("Port", Integer.toString(server.port()));
	}

}
