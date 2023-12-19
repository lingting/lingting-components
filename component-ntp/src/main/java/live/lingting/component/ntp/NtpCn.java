package live.lingting.component.ntp;

import live.lingting.component.core.util.ThreadUtils;
import live.lingting.component.core.value.CycleValue;
import live.lingting.component.core.value.StepValue;
import live.lingting.component.core.value.WaitValue;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.UnaryOperator;

/**
 * 中国 ntp 类
 *
 * @author lingting 2023/2/1 14:10
 */
@Slf4j
@UtilityClass
public class NtpCn {

	/**
	 * 初始化间隔, 单位: 秒
	 */
	public static final StepValue STEP_INIT = StepValue.simple(1, null, 10L);

	public static final ZoneOffset DEFAULT_ZONE_OFFSET = ZoneOffset.of("+8");

	public static final ZoneId DEFAULT_ZONE_ID = DEFAULT_ZONE_OFFSET.normalized();

	/**
	 * 中国授时中心, 使用域名请求超时的话, 解析IP,然后直接请IP
	 */
	public static final String DEFAULT_TIME_SERVER = "ntp.ntsc.ac.cn";

	static WaitValue<Ntp> instance = WaitValue.of();

	static void initNtpCN() {
		CycleValue<Long> value = CycleValue.ofStep(STEP_INIT);
		while (instance.isNull()) {
			CompletableFuture<Ntp> future = CompletableFuture.supplyAsync(NtpCn::newNtp);

			try {
				Long next = value.next();
				Ntp ntp = future.get(next, TimeUnit.SECONDS);
				instance.update(ntp);
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}
			catch (TimeoutException e) {
				log.warn("Ntp初始化超时!");
			}
			catch (Exception e) {
				log.warn("Ntp初始化异常!", e);
			}
			finally {
				future.cancel(true);
			}
		}
	}

	public static Ntp newNtp() {
		return new Ntp(DEFAULT_TIME_SERVER).zoneId(DEFAULT_ZONE_ID);
	}

	@SneakyThrows
	public static Ntp instance() {
		return instance.compute(new UnaryOperator<Ntp>() {
			@Override
			@SneakyThrows
			public Ntp apply(Ntp v) {
				if (v != null) {
					return v;
				}

				ThreadUtils.execute("NTP-INIT", NtpCn::initNtpCN);
				return instance.notNull();
			}
		});
	}

	public long diff() {
		return instance().diff();
	}

	public long currentMillis() {
		return instance().currentMillis();
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
