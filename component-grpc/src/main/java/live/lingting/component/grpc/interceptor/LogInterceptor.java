package live.lingting.component.grpc.interceptor;

import cn.hutool.core.convert.Convert;
import live.lingting.component.grpc.enums.GrpcLogType;
import live.lingting.component.grpc.log.GrpcLogHandler;
import live.lingting.component.grpc.constant.GrpcConstants;
import live.lingting.component.grpc.log.GrpcLog;
import io.grpc.Attributes;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import live.lingting.component.core.StopWatch;
import live.lingting.component.core.constant.GlobalConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.Supplier;

/**
 * 服务端注册要确保在traceId后面
 *
 * @author lingting 2023-04-13 13:29
 */
@Slf4j
@Order(GrpcConstants.ORDER_LOG)
@RequiredArgsConstructor
public class LogInterceptor implements ServerInterceptor, ClientInterceptor {

	private final GrpcLogHandler handler;

	@Override
	public <S, R> ClientCall<S, R> interceptCall(MethodDescriptor<S, R> method, CallOptions callOptions, Channel next) {
		GrpcLog grpcLog = new GrpcLog();
		grpcLog.setType(GrpcLogType.SEND);
		grpcLog.setMethod(method.getBareMethodName());
		grpcLog.setService(method.getServiceName());

		try {
			return timing(() -> next.newCall(method, callOptions), grpcLog);
		}
		finally {
			resolve(next.authority(), grpcLog);
			handler.save(grpcLog);
		}
	}

	@Override
	public <S, R> ServerCall.Listener<S> interceptCall(ServerCall<S, R> call, Metadata headers,
			ServerCallHandler<S, R> next) {

		GrpcLog grpcLog = new GrpcLog();
		grpcLog.setType(GrpcLogType.SEND);
		MethodDescriptor<S, R> method = call.getMethodDescriptor();
		grpcLog.setMethod(method.getBareMethodName());
		grpcLog.setService(method.getServiceName());

		try {
			return timing(() -> next.startCall(call, headers), grpcLog);
		}
		finally {
			resolve(call, grpcLog);
			handler.save(grpcLog);
		}
	}

	<T> T timing(Supplier<T> supplier, GrpcLog grpcLog) {
		StopWatch watch = new StopWatch();
		try {
			watch.start();
			return supplier.get();
		}
		finally {
			watch.stop();
			grpcLog.setTime(watch.timeMillis());
		}
	}

	<S, R> void resolve(ServerCall<S, R> call, GrpcLog grpcLog) {
		Attributes attributes = call.getAttributes();
		SocketAddress address = attributes.get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
		if (address instanceof InetSocketAddress) {
			InetSocketAddress socketAddress = (InetSocketAddress) address;
			InetAddress inetAddress = socketAddress.getAddress();
			grpcLog.setHost(inetAddress.getHostAddress());
			grpcLog.setPort(Convert.toStr(socketAddress.getPort()));
		}
	}

	void resolve(String authority, GrpcLog grpcLog) {
		String[] split = authority.split(GlobalConstants.COLON);
		grpcLog.setHost(split[0]);
		if (split.length > 1) {
			grpcLog.setPort(split[1]);
		}
	}

}
