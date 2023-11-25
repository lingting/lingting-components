package live.lingting.component.bcpkix;

import live.lingting.component.core.util.LocalDateTimeUtils;
import lombok.Getter;
import org.bouncycastle.operator.OperatorCreationException;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;

/**
 * 自用自颁证书
 *
 * @author lingting 2023-11-22 21:00
 */
@Getter
@SuppressWarnings("java:S1874")
public class BcpkixCertificate {

	private final PublicKey publicKey;

	private final PrivateKey privateKey;

	private final X509Certificate raw;

	public BcpkixCertificate(String subject)
			throws NoSuchAlgorithmException, CertificateException, OperatorCreationException {
		this(subject, BcpkixUtils.keyPair("RSA"));
	}

	public BcpkixCertificate(String subject, KeyPair keyPair) throws CertificateException, OperatorCreationException {
		this(keyPair, BcpkixUtils.jcaX509Certificate(subject, keyPair));
	}

	public BcpkixCertificate(KeyPair keyPair, X509Certificate raw) {
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		this.raw = raw;
	}

	public String publicKey() {
		return BcpkixUtils.publicKey(getPublicKey());
	}

	public String privateKey() {
		return BcpkixUtils.privateKey(getPrivateKey());
	}

	public String certificate() throws CertificateEncodingException {
		return BcpkixUtils.certificate(raw);
	}

	public LocalDateTime start() {
		return LocalDateTimeUtils.parse(raw.getNotBefore());
	}

	public LocalDateTime end() {
		return LocalDateTimeUtils.parse(raw.getNotAfter());
	}

	public X500Principal subject() {
		return raw.getSubjectX500Principal();
	}

	public X500Principal issuer() {
		return raw.getIssuerX500Principal();
	}

	public BigInteger serial() {
		return raw.getSerialNumber();
	}

	public String signatureAlgorithm() {
		return raw.getSigAlgName();
	}

}
