package live.lingting.component.elasticsearch.wrapper;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.util.ObjectBuilder;
import live.lingting.component.elasticsearch.EFunction;
import live.lingting.component.elasticsearch.util.ElasticSearchUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author lingting 2023-06-06 17:17
 */
public class QueryWrapper extends AbstractQueryWrapper {

	private QueryWrapper() {
		super();
	}

	public static <E, T> String field(EFunction<E, T> func) {
		Field field = ElasticSearchUtils.resolveField(func);
		return field.getName();
	}

	public static <E, T> Query term(EFunction<E, T> func, T obj) {
		return term(func, obj, builder -> builder);
	}

	public static <E, T> Query term(EFunction<E, T> func, T obj,
			Function<TermQuery.Builder, ObjectBuilder<TermQuery>> operator) {
		String field = field(func);
		return term(field, obj, operator);
	}

	public static <E, T> Query terms(EFunction<E, T> func, Collection<T> objects) {
		String field = field(func);
		return terms(field, objects);
	}

	/**
	 * 大于
	 */
	public static <E, T> Query lt(EFunction<E, T> func, T obj) {
		String field = field(func);
		return lt(field, obj);
	}

	/**
	 * 大于等于
	 */
	public static <E, T> Query le(EFunction<E, T> func, T obj) {
		String field = field(func);
		return le(field, obj);
	}

	/**
	 * 小于
	 */
	public static <E, T> Query gt(EFunction<E, T> func, T obj) {
		String field = field(func);
		return gt(field, obj);
	}

	/**
	 * 小于等于
	 */
	public static <E, T> Query ge(EFunction<E, T> func, T obj) {
		String field = field(func);
		return ge(field, obj);
	}

	/**
	 * 大于等于 start 小于等于 end
	 */
	public static <E, T> Query between(EFunction<E, T> func, T start, T end) {
		String field = field(func);
		return between(field, start, end);
	}

	public static <E> Query exists(EFunction<E, ?> func) {
		String field = field(func);
		return exists(field);
	}

	public static <E> Query notExists(EFunction<E, ?> func) {
		String field = field(func);
		return notExists(field);
	}

	public static <E, T> Query wildcardAll(EFunction<E, T> func, T obj) {
		String field = field(func);
		return wildcardAll(field, obj);
	}

	public static <E, T> Query wildcard(EFunction<E, T> func, T obj) {
		String field = field(func);
		return wildcard(field, obj);
	}
	// endregion

}
