package live.lingting.component.actuator.health;

import live.lingting.component.core.thread.ThreadPool;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2023-07-25 14:35
 */
@Component
public class ThreadPoolHealthIndicator extends AbstractHealthIndicator {

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		ThreadPool instance = ThreadPool.instance();
		ThreadPoolExecutor pool = instance.getPool();

		builder.status(instance.isRunning() ? Status.UP : Status.DOWN)
			.withDetail("CorePoolSize", pool.getCorePoolSize())
			.withDetail("TaskCount", pool.getTaskCount())
			.withDetail("ActiveCount", pool.getActiveCount())
			.withDetail("MaximumPoolSize", pool.getMaximumPoolSize());
	}

}
