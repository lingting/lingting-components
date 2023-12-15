package live.lingting.component.grpc.client.configuration;

import io.grpc.ClientInterceptor;
import live.lingting.component.grpc.client.GrpcClientProvide;
import live.lingting.component.grpc.client.interceptor.GrpcClientTraceIdInterceptor;
import live.lingting.component.grpc.client.properties.GrpcClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2023-04-06 15:21
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcClientProperties.class)
public class GrpcClientAutoConfiguration {

	private final GrpcClientProperties properties;

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = GrpcClientProperties.PREFIX, name = "host")
	public GrpcClientProvide grpcClientProvide(List<ClientInterceptor> interceptors) {
		return new GrpcClientProvide(properties, interceptors);
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientTraceIdInterceptor grpcClientTraceIdInterceptor() {
		return new GrpcClientTraceIdInterceptor(properties);
	}

}
