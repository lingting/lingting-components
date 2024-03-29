package live.lingting.component.grpc.server.properties;

import live.lingting.component.core.util.IdUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-06 15:18
 */
@Data
@ConfigurationProperties(GrpcServerProperties.PREFIX)
public class GrpcServerProperties {

	public static final String PREFIX = "lingting.grpc.server";

	private Integer port;

	private String traceIdKey = IdUtils.TRACE_ID;

	private DataSize messageSize = DataSize.ofMegabytes(512);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTime = TimeUnit.HOURS.toMillis(2);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTimeout = TimeUnit.SECONDS.toMillis(20);

}
