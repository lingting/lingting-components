package live.lingting.component.ntp;

import live.lingting.component.core.util.IpUtils;
import org.junit.jupiter.api.Test;

import java.net.UnknownHostException;
import java.util.Set;

import static live.lingting.component.ntp.NtpConstants.CN_NTP;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2023-11-07 14:41
 */
class NtpTest {

	@Test
	void domain() {
		Ntp ntp = new Ntp(CN_NTP);
		assertNotNull(ntp);
	}

	@Test
	void cn() {
		long millis = NtpCn.currentMillis();
		assertTrue(millis > 0);
	}

	@Test
	void ip() throws UnknownHostException {
		String ip = IpUtils.resolve(CN_NTP);
		Ntp ntp = new Ntp(ip).zoneId(Ntp.DEFAULT_ZONE_ID);
		assertNotNull(ntp);
	}

	@Test
	void factory() throws InterruptedException {
		NtpFactory factory = NtpFactory.getDefault();
		Ntp ntp = factory.initBy();
		System.out.println("host: " + ntp.getHost());
		assertNotNull(ntp);
	}

	@Test
	void factoryForEach() throws InterruptedException {
		NtpFactory factory = NtpFactory.getDefault();
		Set<String> hosts = NtpFactory.getDefaultHosts();

		for (String host : hosts) {
			Ntp ntp = factory.initBy(host);
			System.out.println("host: " + host);
			assertNotNull(host);
		}

	}

}
