package live.lingting.component.ntp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2023-11-07 14:41
 */
class NtpTest {

	@Test
	void domain() {
		Ntp ntp = new Ntp(NtpCn.DEFAULT_TIME_SERVER);
		assertNotNull(ntp);
	}

	@Test
	void cn() {
		long millis = NtpCn.currentMillis();
		assertTrue(millis > 0);
	}

}
