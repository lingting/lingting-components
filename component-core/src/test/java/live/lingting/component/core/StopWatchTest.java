package live.lingting.component.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-04-24 19:31
 */
class StopWatchTest {

	@Test
	void test() {
		StopWatch watch = new StopWatch();
		Assertions.assertFalse(watch.isRunning());
		watch.start();
		Assertions.assertTrue(watch.timeNanos() > 0);
	}

}
