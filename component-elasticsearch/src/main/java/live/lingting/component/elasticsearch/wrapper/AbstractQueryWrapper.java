package live.lingting.component.elasticsearch.wrapper;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.ObjectBuilder;
import live.lingting.component.core.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-11-13 12:15
 */
class AbstractQueryWrapper {

	protected AbstractQueryWrapper() {
	}

	public static FieldValue of(Object obj) {
		return obj instanceof FieldValue ? (FieldValue) obj : FieldValue.of(JsonData.of(obj));
	}

	public static <T> Query term(String field, T obj) {
		return term(field, obj, builder -> builder);
	}

	public static <T> Query term(String field, T obj, Function<TermQuery.Builder, ObjectBuilder<TermQuery>> operator) {
		FieldValue value = of(obj);
		return Query.of(qb -> qb.term(tq -> {
			TermQuery.Builder builder = tq.field(field).value(value);
			return operator.apply(builder);
		}));
	}

	public static <T> Query terms(String field, Collection<T> objects) {
		List<FieldValue> values = new ArrayList<>();

		if (!CollectionUtils.isEmpty(objects)) {
			for (Object object : objects) {
				FieldValue value = QueryWrapper.of(object);
				values.add(value);
			}
		}

		return Query.of(qb -> qb.terms(tq -> tq.field(field).terms(tqf -> tqf.value(values))));
	}

	/**
	 * 大于
	 */
	public static Query lt(String field, Object obj) {
		JsonData value = JsonData.of(obj);
		return Query.of(qb -> qb.range(rq -> rq.field(field).lt(value)));
	}

	/**
	 * 大于等于
	 */
	public static Query le(String field, Object obj) {
		JsonData value = JsonData.of(obj);
		return Query.of(qb -> qb.range(rq -> rq.field(field).lte(value)));
	}

	/**
	 * 小于
	 */
	public static Query gt(String field, Object obj) {
		JsonData value = JsonData.of(obj);
		return Query.of(qb -> qb.range(rq -> rq.field(field).gt(value)));
	}

	/**
	 * 小于等于
	 */
	public static Query ge(String field, Object obj) {
		JsonData value = JsonData.of(obj);
		return Query.of(qb -> qb.range(rq -> rq.field(field).gte(value)));
	}

	/**
	 * 大于等于 start 小于等于 end
	 */
	public static Query between(String field, Object start, Object end) {
		return Query.of(q -> q.bool(bq -> bq.must(
				// 大于等于 start
				le(field, start),
				// 小于等于 end
				ge(field, end)

		)));
	}

	public static Query exists(String field) {
		return Query.of(q -> q.exists(e -> e.field(field)));
	}

	public static Query notExists(String field) {
		return Query.of(q -> q.bool(b -> b.mustNot(mn -> mn.exists(e -> e.field(field)))));
	}

	public static Query should(Query... queries) {
		return should(Arrays.stream(queries).filter(Objects::nonNull).collect(Collectors.toList()));
	}

	public static Query should(List<Query> queries) {
		return Query.of(q -> q.bool(b -> b.should(queries)));
	}

	public static <T> Query wildcardAll(String field, T obj) {
		String format = String.format("*%s*", obj);
		return wildcard(field, format);
	}

	public static <T> Query wildcard(String field, T obj) {
		String value = obj.toString();
		return Query.of(qb -> qb.wildcard(wq -> wq.field(field).value(value)));
	}

	public static Query not(Query... queries) {
		return not(Arrays.stream(queries).filter(Objects::nonNull).collect(Collectors.toList()));
	}

	public static Query not(List<Query> queries) {
		return Query.of(q -> q.bool(b -> b.mustNot(queries)));
	}

}
