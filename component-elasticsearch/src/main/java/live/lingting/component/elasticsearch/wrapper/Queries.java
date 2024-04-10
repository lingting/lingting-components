package live.lingting.component.elasticsearch.wrapper;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.elasticsearch.EFunction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-06-20 14:06
 */
public class Queries<E> {

	private final List<Query> list = new ArrayList<>();

	public Queries<E> add(boolean condition, Supplier<Query> supplier) {
		if (condition) {
			Query query = supplier.get();
			list.add(query);
		}
		return this;
	}

	public Queries<E> add(boolean condition, Query query) {
		if (condition) {
			list.add(query);
		}
		return this;
	}

	public Queries<E> addIfPresent(String field, Object value, BiFunction<String, Object, Query> function) {
		if (isPresent(value)) {
			list.add(function.apply(field, value));
		}
		return this;
	}

	public Queries<E> add(Query query) {
		return add(true, query);
	}

	/**
	 * 判断对象是否存在值
	 */
	public boolean isPresent(Object value) {
		if (value == null) {
			return false;
		}
		if (value instanceof CharSequence) {
			return StringUtils.hasText((CharSequence) value);
		}
		if (value instanceof Collection) {
			return !((Collection<?>) value).isEmpty();
		}
		if (value instanceof Map) {
			return !((Map<?, ?>) value).isEmpty();
		}
		if (value.getClass().isArray()) {
			return Array.getLength(value) > 0;
		}
		return true;
	}

	public List<Query> list() {
		return Collections.unmodifiableList(list);
	}

	public Query[] array() {
		return list().toArray(new Query[0]);
	}

	public Query should() {
		return QueryWrapper.should(array());
	}

	public Query must() {
		return QueryWrapper.must(array());
	}

	// region term

	public <T> Queries<E> term(EFunction<E, T> func, T obj) {
		return term(true, func, obj);
	}

	public <T> Queries<E> term(boolean condition, EFunction<E, T> func, T obj) {
		return add(condition, () -> QueryWrapper.term(func, obj));
	}

	public <T> Queries<E> termIfPresent(EFunction<E, T> func, T value) {
		return term(isPresent(value), func, value);
	}

	// endregion

	// region terms

	public <T> Queries<E> terms(EFunction<E, T> func, Collection<T> objects) {
		return terms(true, func, objects);
	}

	public <T> Queries<E> terms(boolean condition, EFunction<E, T> func, Collection<T> objects) {
		return add(condition, () -> QueryWrapper.terms(func, objects));
	}

	public <T> Queries<E> termsIfPresent(EFunction<E, T> func, Collection<T> values) {
		return terms(isPresent(values), func, values);
	}

	@SafeVarargs
	public final <T> Queries<E> termsIfPresent(EFunction<E, T> func, T... values) {
		if (isPresent(values)) {
			return terms(func, Arrays.stream(values).collect(Collectors.toList()));
		}
		return this;
	}
	// endregion

	// region wildcard
	public <T> Queries<E> wildcard(EFunction<E, T> func, T obj) {
		return wildcard(true, func, obj);
	}

	public <T> Queries<E> wildcard(boolean condition, EFunction<E, T> func, T obj) {
		return add(condition, () -> QueryWrapper.wildcard(func, obj));
	}

	public <T> Queries<E> wildcardIfPresent(EFunction<E, T> func, T obj) {
		return wildcard(isPresent(obj), func, obj);
	}

	public <T> Queries<E> wildcardAll(EFunction<E, T> func, T obj) {
		return wildcardAll(true, func, obj);
	}

	public <T> Queries<E> wildcardAll(boolean condition, EFunction<E, T> func, T obj) {
		return add(condition, () -> QueryWrapper.wildcardAll(func, obj));
	}

	public <T> Queries<E> wildcardAllIfPresent(EFunction<E, T> func, T obj) {
		return wildcardAll(isPresent(obj), func, obj);
	}

	// endregion

	public Queries<E> exists(EFunction<E, ?> func) {
		Query query = QueryWrapper.exists(func);
		return add(query);
	}

	public Queries<E> notExists(EFunction<E, ?> func) {
		Query query = QueryWrapper.notExists(func);
		return add(query);
	}

	public Queries<E> should(Queries<E> queries) {
		return should(queries.array());
	}

	public Queries<E> should(Query... queries) {
		Query query = QueryWrapper.should(queries);
		return add(query);
	}

	public Queries<E> must(Queries<E> queries) {
		return must(queries.array());
	}

	public Queries<E> must(Query... queries) {
		Query query = QueryWrapper.must(queries);
		return add(query);
	}

	public Queries<E> not(Query... queries) {
		return not(Arrays.stream(queries).filter(Objects::nonNull).collect(Collectors.toList()));
	}

	public Queries<E> not(List<Query> queries) {
		if (CollectionUtils.isEmpty(queries)) {
			return this;
		}

		return add(QueryWrapper.not(queries));
	}

	// region range

	/**
	 * 小于
	 */
	public <T> Queries<E> lt(EFunction<E, T> func, T obj) {
		return lt(true, func, obj);
	}

	public <T> Queries<E> lt(boolean condition, EFunction<E, T> func, T obj) {
		return add(condition, () -> QueryWrapper.lt(func, obj));
	}

	public <T> Queries<E> ltIfPresent(EFunction<E, T> func, T obj) {
		return lt(isPresent(obj), func, obj);
	}

	/**
	 * 小于等于
	 */
	public <T> Queries<E> le(EFunction<E, T> func, T obj) {
		return le(true, func, obj);
	}

	public <T> Queries<E> le(boolean condition, EFunction<E, T> func, T obj) {
		return add(condition, () -> QueryWrapper.le(func, obj));
	}

	public <T> Queries<E> leIfPresent(EFunction<E, T> func, T obj) {
		return le(isPresent(obj), func, obj);
	}

	/**
	 * 大于
	 */
	public <T> Queries<E> gt(EFunction<E, T> func, T obj) {
		return gt(true, func, obj);
	}

	public <T> Queries<E> gt(boolean condition, EFunction<E, T> func, T obj) {
		return add(condition, () -> QueryWrapper.gt(func, obj));
	}

	public <T> Queries<E> gtIfPresent(EFunction<E, T> func, T obj) {
		return gt(isPresent(obj), func, obj);
	}

	/**
	 * 大于等于
	 */
	public <T> Queries<E> ge(EFunction<E, T> func, T obj) {
		return ge(true, func, obj);
	}

	public <T> Queries<E> ge(boolean condition, EFunction<E, T> func, T obj) {
		return add(condition, () -> QueryWrapper.ge(func, obj));
	}

	public <T> Queries<E> geIfPresent(EFunction<E, T> func, T obj) {
		return ge(isPresent(obj), func, obj);
	}

	public <T> Queries<E> between(EFunction<E, T> func, T start, T end) {
		return between(true, func, start, end);
	}

	public <T> Queries<E> between(boolean condition, EFunction<E, T> func, T start, T end) {
		return add(condition, () -> QueryWrapper.between(func, start, end));
	}

	public <T> Queries<E> betweenIfPresent(EFunction<E, T> func, T start, T end) {
		return between(isPresent(start) && isPresent(end), func, start, end);
	}
	// endregion

}
