package live.lingting.component.grpc.server.configuration;

import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import live.lingting.component.grpc.server.GrpcServer;
import live.lingting.component.grpc.server.interceptor.GrpcServerTraceIdInterceptor;
import live.lingting.component.grpc.server.properties.GrpcServerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-17 09:15
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerConfiguration {

	private final GrpcServerProperties properties;

	@Bean
	@ConditionalOnMissingBean
	@SuppressWarnings("java:S1452")
	public ServerBuilder<?> grpcServerBuilder(List<ServerInterceptor> interceptors, List<BindableService> services) {
		ServerBuilder<?> builder = ServerBuilder.forPort(properties.getPort())
			// 单个消息最大大小
			.maxInboundMessageSize((int) properties.getMessageSize().toBytes())
			.keepAliveTime(properties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
			.keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.MILLISECONDS);

		// 按照spring生态的 @Order 排序
		interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
		// 获取一个游标在尾部的迭代器
		ListIterator<ServerInterceptor> iterator = interceptors.listIterator(interceptors.size());
		// 服务端是最后注册的拦截器最先执行, 所以要倒序注册
		while (iterator.hasPrevious()) {
			builder.intercept(iterator.previous());
		}

		// 注册服务
		for (BindableService service : services) {
			builder.addService(service);
		}

		return builder;
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcServer grpcServer(ServerBuilder<?> builder) {
		return new GrpcServer(builder.build());
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcServerTraceIdInterceptor grpcServerTraceIdInterceptor() {
		return new GrpcServerTraceIdInterceptor(properties);
	}

}
