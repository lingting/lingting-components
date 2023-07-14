package live.lingting.component.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * @author lingting 2022/12/8 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageScrollResult<T> {

	private List<T> records;

	private Object cursor;

	private Long total;

	public static <T> PageScrollResult<T> of(List<T> collection, Object cursor) {
		return new PageScrollResult<>(collection, cursor, 0L);
	}

	public static <T> PageScrollResult<T> empty() {
		return new PageScrollResult<>(Collections.emptyList(), Collections.emptyList(), 0L);
	}

}
