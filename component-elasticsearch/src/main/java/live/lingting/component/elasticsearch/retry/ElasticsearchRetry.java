package live.lingting.component.elasticsearch.retry;

import live.lingting.component.core.function.ThrowingSupplier;
import live.lingting.component.core.retry.Retry;
import live.lingting.component.core.retry.RetryFunction;
import live.lingting.component.elasticsearch.properties.ElasticsearchProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

import static live.lingting.component.elasticsearch.util.ElasticSearchUtils.isVersionConflictException;

/**
 * @author lingting 2023-12-19 14:19
 */
public class ElasticsearchRetry<T> extends Retry<T> {

	public ElasticsearchRetry(ElasticsearchProperties.Retry retry, ThrowingSupplier<T> supplier) {
		super(supplier, new ElasticsearchRetryFunction(retry));
	}

	@Getter
	@RequiredArgsConstructor
	public static class ElasticsearchRetryFunction implements RetryFunction {

		protected final ElasticsearchProperties.Retry retry;

		protected int versionConflictCount = 0;

		protected int count = 0;

		@Override
		public boolean allowRetry(int retryCount, Exception e) {
			// 版本控制异常
			if (isVersionConflictException(e)) {
				return allowVersionConflictRetry();
			}

			// 其他异常, 已重试次数小于设置重试次数
			boolean allowed = retryCount < retry.getMaxRetryCount();
			if (allowed) {
				count++;
			}
			return allowed;
		}

		protected boolean allowVersionConflictRetry() {
			// 是否允许重试
			boolean allowed =
					// 无限重试
					retry.getVersionConflictMaxRetryCount() < 0
							// 已重试次数小于设置重试次数
							|| versionConflictCount < retry.getVersionConflictMaxRetryCount();

			// 允许重试时, 计数
			if (allowed) {
				versionConflictCount++;
			}

			return allowed;
		}

		@Override
		public Duration getDelay(int retryCount, Exception e) {
			if (isVersionConflictException(e)) {
				return retry.getVersionConflictDelay();
			}
			return retry.getDelay();
		}

	}

}
