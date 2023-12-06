package live.lingting.component.elasticsearch.wrapper;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import live.lingting.component.elasticsearch.EFunction;
import live.lingting.component.elasticsearch.util.ElasticSearchUtils;
import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-06-06 19:14
 */
@UtilityClass
public class SortWrapper {

	public static SortOptions sort(String field, Boolean desc) {
		return sort(field, Boolean.TRUE.equals(desc) ? SortOrder.Desc : SortOrder.Asc);
	}

	public static SortOptions sort(String field, SortOrder order) {
		return SortOptions.of(so -> so.field(fs -> fs.field(field).order(order)));
	}

	public static SortOptions desc(String field) {
		return sort(field, SortOrder.Desc);
	}

	public static SortOptions asc(String field) {
		return sort(field, SortOrder.Asc);
	}

	public static <E, T> SortOptions sort(EFunction<E, T> func, Boolean desc) {
		return sort(func, Boolean.TRUE.equals(desc) ? SortOrder.Desc : SortOrder.Asc);
	}

	public static <E, T> SortOptions sort(EFunction<E, T> func, SortOrder order) {
		String field = ElasticSearchUtils.resolveField(func).getName();
		return SortOptions.of(so -> so.field(fs -> fs.field(field).order(order)));
	}

	public static <E, T> SortOptions desc(EFunction<E, T> func) {
		return sort(func, SortOrder.Desc);
	}

	public static <E, T> SortOptions asc(EFunction<E, T> func) {
		return sort(func, SortOrder.Asc);
	}

}
