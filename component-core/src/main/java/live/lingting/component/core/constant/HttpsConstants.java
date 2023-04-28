package live.lingting.component.core.constant;

import lombok.experimental.UtilityClass;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author lingting 2023/1/31 14:04
 */
@UtilityClass
@SuppressWarnings({ "java:S5527", "java:S2386", "java:S4830" })
public class HttpsConstants {

	/**
	 * 默认信任全部的域名校验器
	 */
	public static final HostnameVerifier HOSTNAME_VERIFIER = (s, sslSession) -> true;

	public static final KeyManager[] KEY_MANAGERS = null;

	public static final X509TrustManager TRUST_MANAGER;

	public static final TrustManager[] TRUST_MANAGERS;

	static {
		TRUST_MANAGER = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				//
			}

			@Override
			public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
				//
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		};
		TRUST_MANAGERS = new TrustManager[] { TRUST_MANAGER };
	}

}
