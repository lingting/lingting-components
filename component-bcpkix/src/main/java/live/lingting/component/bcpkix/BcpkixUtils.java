package live.lingting.component.bcpkix;

import live.lingting.component.core.util.LocalDateTimeUtils;
import live.lingting.component.core.util.StringUtils;
import lombok.experimental.UtilityClass;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Date;

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

	public static X509Certificate jcaX509Certificate(String domain, KeyPair keyPair)
			throws CertificateException, OperatorCreationException {
		// 有效期从昨天开始, 到 99年后
		LocalDateTime start = LocalDateTime.now().minusDays(1);
		LocalDateTime end = start.plusYears(99);

		// 主体
		X500Name issuer = new X500Name("CN=" + domain);
		// 简介
		X500Name subject = new X500Name("CN=" + domain);
		// 序列id
		BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
		// 有效期时间
		Date notBefore = LocalDateTimeUtils.toDate(start);
		Date notAfter = LocalDateTimeUtils.toDate(end);
		// 证书公钥
		SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());

		// 构造器
		X509v3CertificateBuilder builder = new X509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject,
				publicKeyInfo);

		// 使用 SHA256withRSA 签名算法, 使用私钥进行签名
		ContentSigner signer = new JcaContentSignerBuilder("SHA256withRSA").build(keyPair.getPrivate());

		// 生成证书
		X509CertificateHolder certificateHolder = builder.build(signer);

		// 将证书转换为 Java X509Certificate 格式
		return new JcaX509CertificateConverter().getCertificate(certificateHolder);
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
