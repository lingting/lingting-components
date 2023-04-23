package live.lingting.component.core.page;

import live.lingting.component.core.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author lingting 2022/12/8 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageScrollResult<T> {

	private List<Object> nextCursor;

	private Collection<T> records;

	private Long total;

	public static <T> PageScrollResult<T> of(Collection<T> collection, Long total, Object... nextCursor) {
		return of(collection, total, CollectionUtils.toList(nextCursor));
	}

	public static <T> PageScrollResult<T> of(Collection<T> collection, Long total, List<Object> nextCursor) {
		return new PageScrollResult<>(nextCursor, collection, total);
	}

	public static <T> PageScrollResult<T> empty() {
		return new PageScrollResult<>(Collections.emptyList(), Collections.emptyList(), 0L);
	}

}
