package live.lingting.component.ntp;

import live.lingting.component.core.thread.ThreadPool;
import live.lingting.component.core.util.IpUtils;
import live.lingting.component.core.util.ThreadUtils;
import live.lingting.component.core.value.CycleValue;
import live.lingting.component.core.value.StepValue;
import live.lingting.component.core.value.cycle.StepCycleValue;
import live.lingting.component.core.value.step.LongStepValue;
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

import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN;
import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN1;
import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN2;
import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN3;
import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN4;
import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN5;
import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN6;
import static live.lingting.component.ntp.NtpConstants.TIME_ALIYUN7;
import static live.lingting.component.ntp.NtpConstants.TIME_APPLE;
import static live.lingting.component.ntp.NtpConstants.TIME_ASIA;
import static live.lingting.component.ntp.NtpConstants.TIME_CN;
import static live.lingting.component.ntp.NtpConstants.TIME_NIST;
import static live.lingting.component.ntp.NtpConstants.TIME_NTSC;
import static live.lingting.component.ntp.NtpConstants.TIME_POOL;
import static live.lingting.component.ntp.NtpConstants.TIME_WINDOWS;

/**
 * @author lingting 2023-12-27 15:47
 */
@Slf4j
@SuppressWarnings("java:S6548")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class NtpFactory {

	private static final String[] HOSTS = { TIME_WINDOWS, TIME_NIST, TIME_APPLE, TIME_ASIA, TIME_CN, TIME_NTSC,
			TIME_POOL,

			TIME_ALIYUN, TIME_ALIYUN1, TIME_ALIYUN2, TIME_ALIYUN3, TIME_ALIYUN4, TIME_ALIYUN5, TIME_ALIYUN6,
			TIME_ALIYUN7,

	};

	public static final StepValue<Long> STEP_INIT = new LongStepValue(1, null, Long.valueOf(10));

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
		CycleValue<Long> cycle = new StepCycleValue<>(STEP_INIT);
		for (String host : hosts) {
			// 如果所有都被拉黑了
			if (blockHosts.size() == hosts.size()) {
				// 情况, 重新开始
				blockHosts.clear();
			}

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
			catch (Exception e) {
				log.error("Ntp初始化失败! host: {}; message: {}", host, e.getMessage());
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
