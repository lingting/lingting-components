package live.lingting.component.core.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2023-12-21 14:09
 */
class IpUtilsTest {

	String ip1 = "192.168.000.1";

	String ip2 = "256.0.0.1";

	String ip3 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";

	String ip4 = "53543aa";

	@Test
	void isIpv4() {
		assertTrue(IpUtils.isIpv4(ip1));
		assertFalse(IpUtils.isIpv4(ip2));
		assertFalse(IpUtils.isIpv4(ip3));
		assertFalse(IpUtils.isIpv4(ip4));
	}

	@Test
	void isIpv6() {
		assertFalse(IpUtils.isIpv6(ip1));
		assertFalse(IpUtils.isIpv6(ip2));
		assertTrue(IpUtils.isIpv6(ip3));
		assertFalse(IpUtils.isIpv6(ip4));
	}

}
