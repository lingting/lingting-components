package live.lingting.component.bcpkix;

import lombok.Getter;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

/**
 * @author lingting 2023-09-25 10:20
 */
public class Pkcs12 {

	private final KeyStore store;

	@Getter
	private final char[] password;

	@Getter
	private final PrivateKey privateKey;

	@Getter
	private final Certificate certificate;

	public Pkcs12(KeyStore store, char[] password)
			throws KeyStoreException, UnrecoverableKeyException, NoSuchAlgorithmException {
		this.store = store;
		this.password = password;
		// 私钥
		PrivateKey privateKey = null;
		// 证书
		Certificate certificate = null;
		Enumeration<String> aliases = store.aliases();
		while (aliases.hasMoreElements()) {
			String element = aliases.nextElement();
			if (store.isKeyEntry(element)) {
				privateKey = (PrivateKey) store.getKey(element, password);
				certificate = store.getCertificate(element);
			}
		}

		this.privateKey = privateKey;
		this.certificate = certificate;
	}

	public static Pkcs12 ofPfx(InputStream in, char[] password) throws KeyStoreException, CertificateException,
			IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
		KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
		pkcs12.load(in, password);
		return new Pkcs12(pkcs12, password);
	}

	public String privateKey() {
		return "-----BEGIN PRIVATE KEY-----\n"
				+ DatatypeConverter.printBase64Binary(privateKey.getEncoded()).replaceAll("(.{64})", "$1\n")
				+ "\n-----END PRIVATE KEY-----\n";
	}

	public String certificate() throws CertificateEncodingException {
		return "-----BEGIN CERTIFICATE-----\n"
				+ DatatypeConverter.printBase64Binary(certificate.getEncoded()).replaceAll("(.{64})", "$1\n")
				+ "\n-----END CERTIFICATE-----\n";
	}

}
