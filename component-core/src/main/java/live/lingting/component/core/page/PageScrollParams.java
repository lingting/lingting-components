package live.lingting.component.core.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lingting 2022/12/8 16:08
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageScrollParams {

	private Integer size = 10;

	private List<Object> cursors;

}
