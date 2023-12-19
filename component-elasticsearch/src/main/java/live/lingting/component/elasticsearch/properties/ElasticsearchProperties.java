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
		private Duration delay = Duration.ofMillis(200);

	}

}
