package live.lingting.component.ntp;

import live.lingting.component.core.util.IpUtils;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;

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

	@Test
	void ip() throws UnknownHostException {
		String ip = IpUtils.resolve(NtpCn.DEFAULT_TIME_SERVER);
		Ntp ntp = new Ntp(ip).zoneId(Ntp.DEFAULT_ZONE_ID);
		assertNotNull(ntp);
	}

}
