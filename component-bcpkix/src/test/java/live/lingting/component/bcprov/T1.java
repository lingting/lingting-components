package live.lingting.component.bcprov;

import live.lingting.component.bcpkix.Pkcs12;
import live.lingting.component.core.util.FileUtils;
import live.lingting.component.core.util.StreamUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author lingting 2023-09-20 14:14
 */
class T1 {

	File pfxFile = ResourceUtils.getFile("classpath:sg-zone-b-1.global-ipool.com.pfx");

	File passwordFile = ResourceUtils.getFile("classpath:pfx-password.txt");

	T1() throws FileNotFoundException {

	}

	@Test
	void test() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException,
			UnrecoverableKeyException {
		String passwordString = StreamUtils.toString(Files.newInputStream(passwordFile.toPath()));
		char[] password = passwordString.toCharArray();
		InputStream fpxInputStream = Files.newInputStream(pfxFile.toPath());

		Pkcs12 pkcs12 = Pkcs12.ofPfx(fpxInputStream, password);

		// 私钥
		PrivateKey privateKey = pkcs12.getPrivateKey();
		// 证书
		X509Certificate certificate = (X509Certificate) pkcs12.getCertificate();

		Assertions.assertNotNull(privateKey);
		Assertions.assertNotNull(certificate);
		System.out.println(pkcs12.privateKey());
		System.out.println();
		System.out.println(pkcs12.certificate());
		System.out.println();
		File pem = FileUtils.createTemp(".pem");
		try (FileWriter writer = new FileWriter(pem)) {
			writer.write(pkcs12.certificate());
		}

		File pkcs8 = FileUtils.createTemp(".pkcs8");
		try (FileWriter writer = new FileWriter(pkcs8)) {
			writer.write(pkcs12.privateKey());
		}

	}

}
