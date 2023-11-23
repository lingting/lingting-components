package live.lingting.component.grpc.client.configuration;

import io.grpc.ClientInterceptor;
import live.lingting.component.grpc.client.GrpcClientChannel;
import live.lingting.component.grpc.client.interceptor.GrpcClientTraceIdInterceptor;
import live.lingting.component.grpc.client.properties.GrpcClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;

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
	public GrpcClientChannel grpcChannel(List<ClientInterceptor> interceptors) {
		if (properties.getPort() == null || properties.getPort() < 0) {
			throw new IllegalArgumentException(GrpcClientProperties.PREFIX + ".port值不能为: " + properties.getPort());
		}
		return new GrpcClientChannel(properties, builder -> {
			if (!CollectionUtils.isEmpty(interceptors)) {
				// 按照spring生态的 @Order 排序
				interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
				builder.intercept(interceptors);
			}

			return builder;
		});
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientTraceIdInterceptor grpcClientTraceIdInterceptor() {
		return new GrpcClientTraceIdInterceptor(properties);
	}

}
