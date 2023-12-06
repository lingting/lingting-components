package live.lingting.component.ntp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-11-07 14:41
 */
class NtpTest {

	@Test
	void domain() {
		Ntp ntp = new Ntp(NtpCn.DEFAULT_TIME_SERVER);
		Assertions.assertNotNull(ntp);
	}

	@Test
	void ip() {
		Ntp ntp = new Ntp(NtpCn.DEFAULT_TIME_SERVER);
		Assertions.assertNotNull(ntp);
	}

}
