package live.lingting.component.elasticsearch.cursor;

import live.lingting.component.core.function.ThrowingFunction;
import live.lingting.component.core.page.PageScrollResult;
import live.lingting.component.core.util.CollectionUtils;
import lombok.SneakyThrows;

import java.util.List;

/**
 * @author lingting 2023-12-29 11:32
 */
public class ScrollCursor<T> extends Cursor<T> {

	private final ThrowingFunction<String, PageScrollResult<T>> scroll;

	private String scrollId;

	public ScrollCursor(ThrowingFunction<String, PageScrollResult<T>> scroll, String scrollId, List<T> data) {
		this.scroll = scroll;
		this.scrollId = scrollId;
		// 初始数据就为空, 直接结束
		if (CollectionUtils.isEmpty(data)) {
			empty = true;
		}
		else {
			current.addAll(data);
		}
	}

	@SneakyThrows
	@Override
	protected List<T> nextBatchData() {
		PageScrollResult<T> result = scroll.apply(scrollId);
		scrollId = (String) result.getCursor();
		return result.getRecords();
	}

}
