package live.lingting.component.grpc.configuration;

import io.grpc.ClientInterceptor;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.grpc.GrpcClientChannel;
import live.lingting.component.grpc.interceptor.TraceIdInterceptor;
import live.lingting.component.grpc.properties.GrpcClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;

/**
 * @author lingting 2023-04-06 15:21
 */
@AutoConfiguration
@EnableConfigurationProperties(GrpcClientProperties.class)
public class GrpcAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = GrpcClientProperties.PREFIX, name = "host")
	public GrpcClientChannel grpcChannel(GrpcClientProperties properties, List<ClientInterceptor> interceptors) {
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
	public TraceIdInterceptor traceIdInterceptor() {
		return new TraceIdInterceptor();
	}

}