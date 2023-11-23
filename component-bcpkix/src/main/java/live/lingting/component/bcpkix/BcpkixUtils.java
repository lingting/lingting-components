package live.lingting.component.bcpkix;

import live.lingting.component.core.util.StringUtils;
import lombok.experimental.UtilityClass;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

/**
 * @author lingting 2023-11-22 21:54
 */
@UtilityClass
public class BcpkixUtils {

	public static KeyPair keyPair(String algorithm) throws NoSuchAlgorithmException {
		return keyPair(algorithm, 2048);
	}

	public static KeyPair keyPair(String algorithm, int keySize) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
		keyPairGenerator.initialize(keySize);
		return keyPairGenerator.generateKeyPair();
	}

	public static String format(byte[] bytes) {
		String base64 = StringUtils.base64(bytes);
		return format(base64);
	}

	public static String format(String string) {
		return string.replaceAll("(.{64})", "$1\n");
	}

	public static String publicKey(PublicKey publicKey) {
		return "-----BEGIN PUBLIC KEY-----\n" + format(publicKey.getEncoded()) + "\n-----END PUBLIC KEY-----\n";
	}

	public static String privateKey(PrivateKey privateKey) {
		return "-----BEGIN PRIVATE KEY-----\n" + format(privateKey.getEncoded()) + "\n-----END PRIVATE KEY-----\n";
	}

	public static String certificate(Certificate certificate) throws CertificateEncodingException {
		return "-----BEGIN CERTIFICATE-----\n" + format(certificate.getEncoded()) + "\n-----END CERTIFICATE-----\n";
	}

}
