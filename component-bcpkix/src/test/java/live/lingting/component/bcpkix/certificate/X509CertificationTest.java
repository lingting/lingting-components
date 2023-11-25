package live.lingting.component.bcpkix.certificate;

import live.lingting.component.bcpkix.BcpkixCertificate;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.Security;

/**
 * @author lingting 2023-11-22 21:00
 */
class X509CertificationTest {

	@SneakyThrows
	@Test
	void test() {
		Security.addProvider(new BouncyCastleProvider());
		String domain = "main.lingting.live";

		BcpkixCertificate certification = new BcpkixCertificate(domain);

		Assertions.assertEquals("SHA256withRSA", certification.signatureAlgorithm());
		System.out.println(certification.start() + " - " + certification.end());
		Assertions.assertTrue(certification.start().isBefore(certification.end()));

	}

}
