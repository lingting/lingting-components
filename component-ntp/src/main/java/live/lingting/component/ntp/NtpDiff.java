package live.lingting.component.ntp;

import lombok.Getter;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;

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
			NTPUDPClient client = new NTPUDPClient();
			TimeInfo time = client.getTime(InetAddress.getByName(host));
			system = System.currentTimeMillis();
			ntp = time.getMessage().getTransmitTimeStamp().getTime();
			diff = ntp - system;
		}
		catch (Exception e) {
			throw new NtpException("ntp获取时差异常!", e);
		}
	}

}
