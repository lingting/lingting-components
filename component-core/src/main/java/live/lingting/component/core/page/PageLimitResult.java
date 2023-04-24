package live.lingting.component.core.page;

import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lingting 2022/9/22 15:53
 */
@Data
public class PageLimitResult<T> {

	private Long total = 0L;

	private List<T> records = Collections.emptyList();

	public PageLimitResult(List<T> records) {
		if (!CollectionUtils.isEmpty(records)) {
			this.total = (long) records.size();
			this.records = records;
		}
	}

	public PageLimitResult(List<T> records, long total) {
		this.records = records;
		this.total = total;
	}

	public static <T> PageLimitResult<T> of(List<T> records) {
		return new PageLimitResult<>(records);
	}

	public static <T> PageLimitResult<T> of(List<T> records, long total) {
		return new PageLimitResult<>(records, total);
	}

	/**
	 * 转换返回数据类型
	 * @param function 单个数据转换方法
	 * @return live.lingting.votes.component.core.KocPageResult<E>
	 */
	public <E> PageLimitResult<E> convert(Function<T, E> function) {
		List<E> collect = getRecords().stream().map(function).collect(Collectors.toList());
		return of(collect, getTotal());
	}

}
