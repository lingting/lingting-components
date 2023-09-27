package live.lingting.component.core.page;

import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lingting 2022/9/19 21:16
 */
@Data
public class PageLimitParams {

	/**
	 * 排序的 Field 部分的正则
	 */
	public static final String SORT_FILED_REGEX = "(([A-Za-z0-9_]{1,10}\\.)?[A-Za-z0-9_]{1,64})";

	/**
	 * 排序的 order 部分的正则
	 */
	public static final String SORT_FILED_ORDER = "(desc|asc)";

	/**
	 * 完整的排序规则正则
	 */
	public static final String SORT_REGEX = "^" + SORT_FILED_REGEX + "(," + SORT_FILED_ORDER + ")*$";

	/**
	 * 默认的当前页数的参数名
	 */
	public static final String DEFAULT_PAGE_PARAMETER = "page";

	/**
	 * 默认的单页条数的参数名
	 */
	public static final String DEFAULT_SIZE_PARAMETER = "size";

	/**
	 * 默认的排序参数的参数名
	 */
	public static final String DEFAULT_SORT_PARAMETER = "sort";

	/**
	 * 升序关键字
	 */
	public static final String ASC = "asc";

	/**
	 * 默认的最大单页条数
	 */
	public static final int DEFAULT_MAX_PAGE_SIZE = 100;

	/**
	 * SQL 关键字
	 */
	public static final Set<String> SQL_KEYWORDS = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("master",
			"truncate", "insert", "select", "delete", "update", "declare", "alter", "drop", "sleep")));

	private Long page;

	private Long size;

	private List<Sort> sorts;

	public Long getPage() {
		return page == null || page < 1 ? 1 : page;
	}

	public Long getSize() {
		return size == null || size < 1 ? 10 : size;
	}

	/**
	 * limit限制起始数据索引
	 */
	public long start() {
		return (getPage() - 1) * getSize();
	}

	public List<Sort> getSorts() {
		return sorts == null ? new ArrayList<>(0) : sorts;
	}

	@Data
	public static class Sort {

		/**
		 * 排序字段
		 */
		private String field;

		/**
		 * 是否倒序
		 */
		private Boolean desc;

	}

}
