package live.lingting.component.bcpkix;

import live.lingting.component.core.util.StringUtils;
import lombok.Getter;

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
@SuppressWarnings("unused")
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
		PrivateKey priKey = null;
		// 证书
		Certificate localCertificate = null;
		Enumeration<String> aliases = store.aliases();
		while (aliases.hasMoreElements()) {
			String element = aliases.nextElement();
			if (store.isKeyEntry(element)) {
				priKey = (PrivateKey) store.getKey(element, password);
				localCertificate = store.getCertificate(element);
			}
		}

		this.privateKey = priKey;
		this.certificate = localCertificate;
	}

	public static Pkcs12 ofPfx(InputStream in, char[] password) throws KeyStoreException, CertificateException,
			IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
		KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
		pkcs12.load(in, password);
		return new Pkcs12(pkcs12, password);
	}

	public String privateKey() {
		return "-----BEGIN PRIVATE KEY-----\n"
				+ StringUtils.base64(privateKey.getEncoded()).replaceAll("(.{64})", "$1\n")
				+ "\n-----END PRIVATE KEY-----\n";
	}

	public String certificate() throws CertificateEncodingException {
		return "-----BEGIN CERTIFICATE-----\n"
				+ StringUtils.base64(certificate.getEncoded()).replaceAll("(.{64})", "$1\n")
				+ "\n-----END CERTIFICATE-----\n";
	}

}
