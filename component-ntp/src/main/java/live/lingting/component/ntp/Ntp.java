package live.lingting.component.ntp;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * ntp 校时服务
 *
 * @author lingting 2022/11/18 13:40
 */
@Slf4j
public class Ntp {

	public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+0");

	public static final ZoneId DEFAULT_ZONE_ID = DEFAULT_ZONE_OFFSET.normalized();

	@Getter
	private final String host;

	private final long diff;

	private ZoneId zoneId = DEFAULT_ZONE_ID;

	public Ntp(String host) {
		try {
			NtpDiff ntpDiff = new NtpDiff(host);
			this.host = host;
			this.diff = ntpDiff.getDiff();
			log.warn("授时中心时间[{}]与系统时间[{}]差为 {} 毫秒", ntpDiff.getNtp(), ntpDiff.getSystem(), ntpDiff.getDiff());
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

	public long diff() {
		return diff;
	}

	public Instant instant() {
		long millis = currentMillis();
		return Instant.ofEpochMilli(millis);
	}

	public LocalDateTime now() {
		Instant instant = instant();
		return LocalDateTime.ofInstant(instant, zoneId);
	}

	public long plusSeconds(long seconds) {
		return plusMillis(seconds * 1000);
	}

	public long plusMillis(long millis) {
		return currentMillis() + millis;
	}

	public long plus(long time, TimeUnit unit) {
		return plusMillis(unit.toMillis(time));
	}

}
