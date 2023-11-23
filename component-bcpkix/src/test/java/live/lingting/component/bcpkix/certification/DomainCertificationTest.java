package live.lingting.component.bcpkix.certification;

import live.lingting.component.bcpkix.BcpkixUtils;
import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.Security;

/**
 * @author lingting 2023-11-22 21:00
 */
class DomainCertificationTest {

	@SneakyThrows
	@Test
	void test() {
		Security.addProvider(new BouncyCastleProvider());
		String domain = "main.lingting.live";

		// 生成密钥对
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();

		DomainCertification certification = new DomainCertification(domain, keyPair);

		Assertions.assertEquals(BcpkixUtils.publicKey(keyPair.getPublic()), certification.publicKey());
		Assertions.assertEquals(BcpkixUtils.privateKey(keyPair.getPrivate()), certification.privateKey());
		Assertions.assertEquals(domain, certification.getDomain());
		Assertions.assertEquals("SHA256WITHRSA", certification.signatureAlgorithm());
		System.out.println(certification.start() + " - " + certification.end());
		Assertions.assertTrue(certification.start().isBefore(certification.end()));

	}

}
