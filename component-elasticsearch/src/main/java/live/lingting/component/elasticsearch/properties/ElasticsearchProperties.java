package live.lingting.component.elasticsearch.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author lingting 2023-12-11 10:22
 */
@Data
@ConfigurationProperties(prefix = ElasticsearchProperties.PREFIX)
public class ElasticsearchProperties {

	public static final String PREFIX = "lingting.elasticsearch";

	/**
	 * 重试配置
	 */
	private Retry retry = new Retry();

	@Data
	public static class Retry {

		/**
		 * 最大重试次数
		 */
		private int maxRetryCount = 3;

		/**
		 * 每次重试延迟
		 */
		private Duration delay = Duration.ofMillis(50);

		/**
		 * 触发版本冲突时重试次数, 小于0表示无限重试
		 * <p>
		 * 此重试独立计数, 不论是否达到 {@link Retry#getMaxRetryCount()} 均会按照此配置进行重试
		 * </p>
		 */
		private int versionConflictMaxRetryCount = 50;

		/**
		 * 版本冲突重试延迟
		 */
		private Duration versionConflictDelay = Duration.ofMillis(500);

	}

}
