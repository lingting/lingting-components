package live.lingting.component.core.retry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author lingting 2023-10-23 18:59
 */
@Getter
@RequiredArgsConstructor
public class RetryValue<T> {

	private final T value;

	/**
	 * 是否执行成功
	 */
	private final boolean success;

	private final List<RetryLog<T>> logs;

	public T get() throws Exception {
		if (isSuccess()) {
			return value;
		}
		throw logs.get(0).getException();
	}

}
