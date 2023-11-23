package live.lingting.component.bcpkix.certification;

import live.lingting.component.bcpkix.BcpkixUtils;
import live.lingting.component.core.util.LocalDateTimeUtils;
import lombok.Getter;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * 域名证书
 *
 * @author lingting 2023-11-22 21:00
 */
@SuppressWarnings("java:S1874")
public class DomainCertification {

	@Getter
	private final String domain;

	@Getter
	private final PublicKey publicKey;

	@Getter
	private final PrivateKey privateKey;

	private final X509Certificate certificate;

	public DomainCertification(String domain) throws NoSuchAlgorithmException, CertificateEncodingException,
			SignatureException, NoSuchProviderException, InvalidKeyException {
		this(domain, BcpkixUtils.keyPair("RSA"));
	}

	public DomainCertification(String domain, KeyPair keyPair) throws CertificateEncodingException,
			NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException {
		this(domain, keyPair, generator -> {
		});
	}

	public DomainCertification(String domain, KeyPair keyPair, Consumer<X509V3CertificateGenerator> consumer)
			throws CertificateEncodingException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException,
			InvalidKeyException {
		// 有效期从昨天开始, 到 99年后
		LocalDateTime start = LocalDateTime.now().minusDays(1);
		LocalDateTime end = start.plusYears(99);

		X509Name subject = new X509Name("CN=" + domain);

		X509V3CertificateGenerator generator = new X509V3CertificateGenerator();
		generator.setSubjectDN(subject);
		generator.setIssuerDN(subject);
		generator.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
		generator.setNotBefore(LocalDateTimeUtils.toDate(start));
		generator.setNotAfter(LocalDateTimeUtils.toDate(end));
		generator.setPublicKey(keyPair.getPublic());
		generator.setSignatureAlgorithm("SHA256WithRSAEncryption");

		consumer.accept(generator);

		this.domain = domain;
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		this.certificate = generator.generate(keyPair.getPrivate(), "BC");
	}

	public DomainCertification(String domain, KeyPair keyPair, X509Certificate certificate) {
		this.domain = domain;
		this.publicKey = keyPair.getPublic();
		this.privateKey = keyPair.getPrivate();
		this.certificate = certificate;
	}

	public String publicKey() {
		return BcpkixUtils.publicKey(getPublicKey());
	}

	public String privateKey() {
		return BcpkixUtils.privateKey(getPrivateKey());
	}

	public String certificate() throws CertificateEncodingException {
		return BcpkixUtils.certificate(certificate);
	}

	public LocalDateTime start() {
		return LocalDateTimeUtils.parse(certificate.getNotBefore());
	}

	public LocalDateTime end() {
		return LocalDateTimeUtils.parse(certificate.getNotAfter());
	}

	public Principal subject() {
		return certificate.getSubjectDN();
	}

	public Principal issuer() {
		return certificate.getIssuerDN();
	}

	public BigInteger serialNumber() {
		return certificate.getSerialNumber();
	}

	public String signatureAlgorithm() {
		return certificate.getSigAlgName();
	}

}
