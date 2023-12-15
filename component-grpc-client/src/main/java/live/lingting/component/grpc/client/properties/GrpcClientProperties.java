package live.lingting.component.grpc.client.properties;

import live.lingting.component.core.util.IdUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-06 15:18
 */
@Data
@ConfigurationProperties(GrpcClientProperties.PREFIX)
public class GrpcClientProperties {

	public static final String PREFIX = "lingting.grpc.client";

	private String host;

	private Integer port = 80;

	private String traceIdKey = IdUtils.TRACE_ID;

	private boolean usePlaintext = true;

	private boolean enableRetry = true;

	private boolean enableKeepAlive = true;

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTime = TimeUnit.MINUTES.toMillis(30);

	/**
	 * 单位: 毫秒
	 */
	private long keepAliveTimeout = TimeUnit.SECONDS.toMillis(2);

}
