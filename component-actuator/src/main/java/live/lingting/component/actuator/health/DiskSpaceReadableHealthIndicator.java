package live.lingting.component.actuator.health;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.system.DiskSpaceHealthIndicator;
import org.springframework.util.unit.DataSize;

import java.io.File;

/**
 * @author lingting 2023-11-23 21:42
 */
@Slf4j
public class DiskSpaceReadableHealthIndicator extends DiskSpaceHealthIndicator {

	private final File path;

	private final DataSize threshold;

	/**
	 * Create a new {@code DiskSpaceHealthIndicator} instance.
	 * @param path the Path used to compute the available disk space
	 * @param threshold the minimum disk space that should be available
	 */
	public DiskSpaceReadableHealthIndicator(File path, DataSize threshold) {
		super(path, threshold);
		this.path = path;
		this.threshold = threshold;
	}

	@Override
	protected void doHealthCheck(Health.Builder builder) throws Exception {
		long diskFreeInBytes = path.getUsableSpace();
		boolean isUp = diskFreeInBytes >= threshold.toBytes();

		DataSize total = DataSize.ofBytes(path.getTotalSpace());
		DataSize free = DataSize.ofBytes(diskFreeInBytes);

		builder.status(isUp ? Status.UP : Status.DOWN)
			.withDetail("total", to(total))
			.withDetail("free", to(free))
			.withDetail("threshold", to(threshold))
			.withDetail("exists", path.exists());
	}

	String to(DataSize size) {
		if (size.toBytes() < 1024) {
			String prefix = "B";
			return String.format("%d%s", size.toBytes(), prefix);
		}
		else if (size.toKilobytes() < 1024) {
			String prefix = "KB";
			return String.format("%d%s", size.toKilobytes(), prefix);
		}
		else if (size.toMegabytes() < 1024) {
			String prefix = "MB";
			return String.format("%d%s", size.toMegabytes(), prefix);
		}
		else if (size.toGigabytes() < 1024) {
			String prefix = "GB";
			return String.format("%d%s", size.toGigabytes(), prefix);
		}
		else {
			String prefix = "TB";
			return String.format("%d%s", size.toTerabytes(), prefix);
		}
	}

}
