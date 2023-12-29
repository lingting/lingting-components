package live.lingting.component.ntp;

import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.core.util.IpUtils;
import live.lingting.component.core.util.ThreadUtils;
import live.lingting.component.core.value.CycleValue;
import live.lingting.component.core.value.StepValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static live.lingting.component.ntp.NtpConstants.CN_NTP;
import static live.lingting.component.ntp.NtpConstants.CN_POOL;
import static live.lingting.component.ntp.NtpConstants.NTP_NTSC;
import static live.lingting.component.ntp.NtpConstants.TIME_APPLE;
import static live.lingting.component.ntp.NtpConstants.TIME_ASIA;
import static live.lingting.component.ntp.NtpConstants.TIME_NIST;
import static live.lingting.component.ntp.NtpConstants.TIME_WINDOWS;

/**
 * @author lingting 2023-12-27 15:47
 */
@Slf4j
@SuppressWarnings("java:S6548")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NtpFactory {

	private static final String[] HOSTS = { TIME_WINDOWS, TIME_NIST, TIME_APPLE, TIME_ASIA, CN_NTP, NTP_NTSC,
			CN_POOL, };

	public static final StepValue<Long> STEP_INIT = StepValue.simple(1, null, 10L);

	public static final NtpFactory INSTANCE = new NtpFactory();

	public static Set<String> getDefaultHosts() {
		return Arrays.stream(HOSTS).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	public static NtpFactory getDefault() {
		return INSTANCE;
	}

	private final Set<String> blockHosts = new HashSet<>();

	public Ntp initBy() throws InterruptedException {
		return initBy(HOSTS);
	}

	public Ntp initBy(String... hosts) throws InterruptedException {
		return initBy(Arrays.asList(hosts));
	}

	public Ntp initBy(Collection<String> hosts) throws InterruptedException {
		CycleValue<Long> cycle = CycleValue.ofStep(STEP_INIT);
		for (String host : hosts) {
			if (blockHosts.contains(host)) {
				log.debug("host[{}]被拉黑! 跳过", host);
				continue;
			}
			try {
				Ntp ntp = initByFuture(cycle, host);
				if (ntp != null) {
					return ntp;
				}
			}
			catch (UnknownHostException e) {
				log.warn("host[{}]无法解析!", host);
				blockHosts.add(host);
			}
			catch (InterruptedException e) {
				throw e;
			}
			catch (TimeoutException e) {
				log.warn("Ntp初始化超时! host: {}", host);
			}
			catch (Exception e) {
				log.error("初始化异常! host: {}", host);
			}
		}
		return null;
	}

	public Ntp initByFuture(CycleValue<Long> cycle, String host)
			throws UnknownHostException, ExecutionException, TimeoutException, InterruptedException {
		String ip = IpUtils.resolve(host);

		ThreadPool instance = ThreadUtils.instance();
		ThreadPoolExecutor executor = instance.getPool();
		CompletableFuture<Ntp> future = CompletableFuture.supplyAsync(() -> new Ntp(host), executor);
		try {
			Ntp ntp = future.get(cycle.next(), TimeUnit.SECONDS);
			if (ntp != null) {
				return ntp;
			}
		}
		finally {
			future.cancel(true);
		}

		future = CompletableFuture.supplyAsync(() -> new Ntp(ip), executor);
		try {
			return future.get(cycle.next(), TimeUnit.SECONDS);
		}
		finally {
			future.cancel(true);
		}
	}

}
