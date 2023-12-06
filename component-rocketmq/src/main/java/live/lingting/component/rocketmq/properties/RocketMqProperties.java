package live.lingting.component.rocketmq.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.unit.DataSize;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-05-26 09:47
 */
@Data
@ConfigurationProperties(RocketMqProperties.PREFIX)
public class RocketMqProperties {

	public static final String PREFIX = "live.lingting.rocketmq";

	private String host;

	private Integer port;

	private Charset charset = StandardCharsets.UTF_8;

	/**
	 * 消息发送超时时间, 单位: 毫秒
	 */
	private long sendTimeout = TimeUnit.MINUTES.toMillis(1);

	/**
	 * 拉取消息超时时间, 单位: 毫秒
	 */
	private long pullTimeout = TimeUnit.MILLISECONDS.toMillis(500);

	/**
	 * 在未拉取到消息时, 间隔多久再次拉取, 单位: 毫秒
	 */
	private long pullInterval = TimeUnit.SECONDS.toMillis(1);

	/**
	 * 消息消费超时时间, 单位: 秒
	 */
	private long consumeTimeout = TimeUnit.MINUTES.toSeconds(10);

	/**
	 * api请求超时时间, 单位: 毫秒
	 */
	private long apiTimeout = TimeUnit.MINUTES.toMillis(1);

	private DataSize batchMaxSize = DataSize.ofMegabytes(1);

	public String address() {
		String nowHost = getHost();
		Integer nowPort = getPort();
		if (nowPort == null || nowPort < 1) {
			return nowHost;
		}
		return String.format("%s:%d", nowHost, nowPort);
	}

}
