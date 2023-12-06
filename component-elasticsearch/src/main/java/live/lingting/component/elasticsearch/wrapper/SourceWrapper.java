package live.lingting.component.elasticsearch.wrapper;

import co.elastic.clients.elasticsearch.core.search.SourceConfig;
import live.lingting.component.elasticsearch.EFunction;
import live.lingting.component.elasticsearch.util.ElasticSearchUtils;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author lingting 2023-07-14 10:14
 */
@UtilityClass
public class SourceWrapper {

	static <E> String value(EFunction<E, ?> function) {
		Field field = ElasticSearchUtils.resolveField(function);
		return field.getName();
	}

	public static SourceConfig includes(String value, String... values) {
		return SourceConfig.of(sc -> sc.filter(sf -> sf.includes(value, values)));
	}

	@SafeVarargs
	public static <E> SourceConfig includes(EFunction<E, ?> function, EFunction<E, ?>... functions) {
		String value = value(function);
		String[] values = Arrays.stream(functions).map(SourceWrapper::value).toArray(String[]::new);
		return includes(value, values);
	}

}
