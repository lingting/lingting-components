package live.lingting.component.actuator.health;

import live.lingting.component.core.thread.ThreadPool;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

/**
 * @author lingting 2023-07-25 14:35
 */
public class ThreadPoolHealthIndicator extends AbstractHealthIndicator {

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		ThreadPool instance = ThreadPool.instance();

		builder.status(instance.isRunning() ? Status.UP : Status.DOWN)
			.withDetail("CorePoolSize", instance.getCorePoolSize())
			.withDetail("TaskCount", instance.getTaskCount())
			.withDetail("ActiveCount", instance.getActiveCount())
			.withDetail("MaximumPoolSize", instance.getMaximumPoolSize());
	}

}
