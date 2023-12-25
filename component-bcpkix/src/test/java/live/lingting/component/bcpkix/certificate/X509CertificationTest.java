package live.lingting.component.bcpkix.certificate;

import live.lingting.component.bcpkix.BcpkixCertificate;
import live.lingting.component.core.util.StringUtils;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.Security;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
		assertTrue(certification.start().isBefore(certification.end()));
		String certificate = certification.certificate();
		String privateKey = certification.privateKey();
		System.out.println(certificate);
		System.out.println("============");
		System.out.println(privateKey);
		assertTrue(StringUtils.hasText(certificate));
		assertTrue(StringUtils.hasText(privateKey));
	}

}
