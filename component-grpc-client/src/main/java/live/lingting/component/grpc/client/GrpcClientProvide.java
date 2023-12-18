package live.lingting.component.grpc.client;

import io.grpc.Channel;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractAsyncStub;
import io.grpc.stub.AbstractBlockingStub;
import io.grpc.stub.AbstractFutureStub;
import io.grpc.stub.AbstractStub;
import live.lingting.component.grpc.client.properties.GrpcClientProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * @author lingting 2023-12-15 14:44
 */
@RequiredArgsConstructor
public class GrpcClientProvide {

	protected final GrpcClientProperties properties;

	protected final List<ClientInterceptor> interceptors;

	public ManagedChannel channel() {
		return channel(properties.getHost(), properties.getPort());
	}

	public ManagedChannel channel(String host, Integer port) {
		return channel(host, port, builder -> builder);
	}

	public ManagedChannel channel(String host, Integer port, UnaryOperator<ManagedChannelBuilder<?>> operator) {
		if (port == null || port < 0) {
			throw new IllegalArgumentException(GrpcClientProperties.PREFIX + ".port值不能为: " + port);
		}
		return channel(String.format("%s:%d", host, port), operator);
	}

	public ManagedChannel channel(String target, UnaryOperator<ManagedChannelBuilder<?>> operator) {
		ManagedChannelBuilder<?> builder = ManagedChannelBuilder.forTarget(target);

		// 开启心跳
		if (properties.isEnableKeepAlive()) {
			builder.keepAliveTime(properties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
				.keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.MILLISECONDS);
		}

		// 使用明文
		if (properties.isUsePlaintext()) {
			builder.usePlaintext();
		}

		// 重试
		if (properties.isEnableRetry()) {
			builder.enableRetry();
		}

		// 注册拦截器
		if (!CollectionUtils.isEmpty(interceptors)) {
			// 按照spring生态的 @Order 排序
			interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
			builder.intercept(interceptors);
		}

		ManagedChannelBuilder<?> apply = operator.apply(builder);
		return apply.build();
	}

	public <R extends AbstractStub<R>> R stub(Channel channel, Function<Channel, R> function) {
		return function.apply(channel);
	}

	public <R extends AbstractAsyncStub<R>> R async(Channel channel, Function<Channel, R> function) {
		return stub(channel, function);
	}

	public <R extends AbstractBlockingStub<R>> R blocking(Channel channel, Function<Channel, R> function) {
		return stub(channel, function);
	}

	public <R extends AbstractFutureStub<R>> R future(Channel channel, Function<Channel, R> function) {
		return stub(channel, function);
	}

}
