package live.lingting.component.ntp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.net.InetAddress;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * ntp 校时服务
 *
 * @author lingting 2022/11/18 13:40
 */
@Slf4j
public class Ntp {

	public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

	public static final ZoneId DEFAULT_ZONE_ID = DEFAULT_ZONE_OFFSET.normalized();

	private final long diff;

	private ZoneId zoneId = DEFAULT_ZONE_ID;

	public Ntp(String host) {
		try {
			diff = diff(host);
			log.warn("授时中心时间与系统时间差为 {} 毫秒", diff);
		}
		catch (Exception e) {
			throw new NtpException("ntp初始化异常!", e);
		}
	}

	public Ntp zoneId(ZoneId zoneId) {
		if (zoneId == null) {
			throw new NtpException("ZoneId must be not null!");
		}
		this.zoneId = zoneId;
		return this;
	}

	public long currentMillis() {
		return System.currentTimeMillis() + diff;
	}

	/**
	 * 切换授时中心
	 * @return 返回新的ntp实例
	 */
	public Ntp use(String host) {
		return new Ntp(host);
	}

	/**
	 * 计算时差
	 */
	public long diff(String host) {
		try {
			NTPUDPClient client = new NTPUDPClient();
			TimeInfo time = client.getTime(InetAddress.getByName(host));
			long systemMillis = System.currentTimeMillis();
			long ntpMillis = time.getMessage().getTransmitTimeStamp().getTime();
			return ntpMillis - systemMillis;
		}
		catch (Exception e) {
			throw new NtpException("ntp初始化异常!", e);
		}
	}

	public LocalDateTime now() {
		long millis = currentMillis();
		Instant instant = Instant.ofEpochMilli(millis);
		return LocalDateTime.ofInstant(instant, zoneId);
	}

}
