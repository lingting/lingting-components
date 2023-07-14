package live.lingting.component.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lingting 2022/12/8 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageScrollParams {

	private Long size = 10L;

	private Object cursor;

	public Long getSize() {
		return size == null || size < 1 ? 10 : size;
	}

}
