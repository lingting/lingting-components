package live.lingting.component.bcpkix;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;

/**
 * @author lingting 2023-11-22 21:55
 */
class BcpkixUtilsTest {

	@SneakyThrows
	@Test
	void keyPair() {
		KeyPair keyPair = BcpkixUtils.keyPair("RSA");
		Assertions.assertNotNull(keyPair);
	}

}
