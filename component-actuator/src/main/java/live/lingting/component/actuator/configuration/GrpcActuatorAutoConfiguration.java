package live.lingting.component.actuator.configuration;

import live.lingting.component.actuator.health.GrpcServerHealthIndicator;
import live.lingting.component.grpc.server.GrpcServer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-07-25 17:29
 */
@ConditionalOnClass(GrpcServer.class)
@AutoConfiguration(before = ActuatorAutoConfiguration.class)
public class GrpcActuatorAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(GrpcServer.class)
	public GrpcServerHealthIndicator grpcServerHealthIndicator(GrpcServer server) {
		return new GrpcServerHealthIndicator(server);
	}

}
