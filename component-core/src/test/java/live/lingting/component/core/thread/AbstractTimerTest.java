package live.lingting.component.core.thread;

import live.lingting.component.core.context.ContextHolder;
import live.lingting.component.core.util.ValueUtils;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lingting 2023-12-20 21:53
 */
class AbstractTimerTest {

	@Test
	void test() throws InterruptedException {
		ContextHolder.start();
		AtomicInteger atomic = new AtomicInteger(0);

		AbstractTimer timer = new AbstractTimer() {

			@Override
			public long getTimeout() {
				// 设置30分钟的执行间隔
				return TimeUnit.MINUTES.toMillis(30);
			}

			@Override
			protected void process() throws Exception {
				atomic.set(atomic.get() + 1);
			}
		};

		timer.onApplicationStart();
		ValueUtils.await(atomic::get, v -> v > 0);
		timer.wake();
		ValueUtils.await(atomic::get, v -> v > 1);
		assertEquals(2, atomic.get());
	}

}
