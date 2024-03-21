package live.lingting.component.ntp;

import lombok.Getter;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * @author lingting 2023-11-07 15:49
 */
@Getter
public class NtpDiff {

	/**
	 * 系统当前毫秒
	 */
	private final long system;

	/**
	 * NTP服务器当前毫秒
	 */
	private final long ntp;

	/**
	 * Ntp - System 毫秒
	 */
	private final long diff;

	public NtpDiff(String host) {
		try {
			NTPUDPClient client = client();
			InetAddress address = InetAddress.getByName(host);
			TimeInfo time = client.getTime(address);
			system = System.currentTimeMillis();
			ntp = time.getMessage().getTransmitTimeStamp().getTime();
			diff = ntp - system;
		}
		catch (SocketTimeoutException e) {
			throw new NtpException("ntp获取时差超时!", e);
		}
		catch (Exception e) {
			throw new NtpException("ntp获取时差异常!", e);
		}
	}

	public NTPUDPClient client() {
		NTPUDPClient client = new NTPUDPClient();
		// 超时时间, 毫秒
		client.setDefaultTimeout(3000);
		return client;
	}

}
