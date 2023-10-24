package live.lingting.component.core.retry;

import live.lingting.component.core.function.ThrowingSupplier;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lingting 2023-10-24 14:33
 */
class RetryTest {

	@SneakyThrows
	@Test
	void test() {
		int expected = 3;

		AtomicInteger atomic = new AtomicInteger(0);
		ThrowingSupplier<Integer> supplier = () -> {
			int i = atomic.get();
			if (i == expected) {
				return i;
			}
			atomic.set(i + 1);
			throw new IllegalStateException("异常");
		};

		Retry<Integer> retry = new Retry<>(supplier, 4, Duration.ZERO);
		RetryValue<Integer> value = retry.value();
		Assertions.assertTrue(value.isSuccess());
		Assertions.assertEquals(expected, value.get());
		Assertions.assertEquals(4, value.getLogs().size());

		atomic.set(0);
		retry = new Retry<>(supplier, 3, Duration.ZERO);
		value = retry.value();
		Assertions.assertFalse(value.isSuccess());
		Assertions.assertNull(value.getValue());
		Assertions.assertEquals(3, value.getLogs().size());

	}

}
