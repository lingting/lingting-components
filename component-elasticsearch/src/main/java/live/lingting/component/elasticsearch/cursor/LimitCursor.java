package live.lingting.component.elasticsearch.cursor;

import live.lingting.component.core.function.ThrowingFunction;
import live.lingting.component.core.page.PageLimitResult;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author lingting 2023-12-29 11:32
 */
public class LimitCursor<T> extends Cursor<T> {

	private final ThrowingFunction<Long, PageLimitResult<T>> limit;

	private long index;

	public LimitCursor(ThrowingFunction<Long, PageLimitResult<T>> limit) {
		this.limit = limit;
		this.index = 1;
	}

	@SneakyThrows
	@Override
	protected List<T> nextBatchData() {
		PageLimitResult<T> result = limit.apply(index);
		index++;
		return result.getRecords();
	}

}
