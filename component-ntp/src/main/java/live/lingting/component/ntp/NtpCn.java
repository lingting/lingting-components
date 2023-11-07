package live.lingting.component.ntp;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * 中国 ntp 类
 *
 * @author lingting 2023/2/1 14:10
 */
@Slf4j
@UtilityClass
public class NtpCn {

	public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

	public static final ZoneId DEFAULT_ZONE_ID = DEFAULT_ZONE_OFFSET.normalized();

	/**
	 * time.7x24s.com 中国国家授时中心, 使用域名请求超时的话, 解析IP,然后直接请IP
	 */
	public static final String DEFAULT_TIME_SERVER = "time.7x24s.com";

	/**
	 * 上海解析出来的ip
	 */
	public static final String DEFAULT_TIME_SERVER_SH_IP = "203.107.6.88";

	static Ntp instance = null;

	public static Ntp instance() {
		if (instance == null) {
			instance = new Ntp(DEFAULT_TIME_SERVER).zoneId(DEFAULT_ZONE_ID);
		}
		return instance;
	}

	public Instant instant() {
		return instance().instant();
	}

	public LocalDateTime now() {
		return instance().now();
	}

	public long plusSeconds(long seconds) {
		return plusMillis(seconds * 1000);
	}

	public long plusMillis(long millis) {
		return instance().plusMillis(millis);
	}

	public long plus(long time, TimeUnit unit) {
		return plusMillis(unit.toMillis(time));
	}

}
