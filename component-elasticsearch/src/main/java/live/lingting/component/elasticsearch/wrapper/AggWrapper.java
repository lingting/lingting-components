package live.lingting.component.elasticsearch.wrapper;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import live.lingting.component.elasticsearch.EFunction;
import live.lingting.component.elasticsearch.util.ElasticSearchUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.function.UnaryOperator;

/**
 * @author lingting 2023-07-14 10:02
 */
@UtilityClass
public class AggWrapper {

	@Getter
	@Setter
	private static int defaultSize = 100;

	static <E> String field(EFunction<E, ?> function) {
		Field field = ElasticSearchUtils.resolveField(function);
		return field.getName();
	}

	public static Aggregation terms(String field) {
		return terms(field, getDefaultSize());
	}

	public static Aggregation terms(String field, Integer size) {
		return terms(field, size, builder -> builder);
	}

	public static <E> Aggregation terms(EFunction<E, ?> function) {
		return terms(field(function));
	}

	public static <E> Aggregation terms(EFunction<E, ?> function, Integer size) {
		return terms(field(function), size);
	}

	public static Aggregation terms(String field, Integer size,
			UnaryOperator<Aggregation.Builder.ContainerBuilder> operator) {
		return Aggregation.of(agg -> {
			Aggregation.Builder.ContainerBuilder builder = agg.terms(ta -> ta.field(field).size(size));
			return operator.apply(builder);
		});
	}

	public static <E> Aggregation terms(EFunction<E, ?> function,
			UnaryOperator<Aggregation.Builder.ContainerBuilder> operator) {
		return terms(function, getDefaultSize(), operator);
	}

	public static <E> Aggregation terms(EFunction<E, ?> function, Integer size,
			UnaryOperator<Aggregation.Builder.ContainerBuilder> operator) {
		return terms(field(function), size, operator);
	}

}
