package live.lingting.component.elasticsearch.builder;

import co.elastic.clients.elasticsearch._types.InlineScript;
import co.elastic.clients.elasticsearch._types.Script;
import co.elastic.clients.json.JsonData;
import live.lingting.component.elasticsearch.EFunction;
import live.lingting.component.elasticsearch.util.ElasticSearchUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2023-06-20 15:32
 */
public class ScriptBuilder<T> {

	public static final String PREFIX_SOURCE = "ctx._source.";

	public static final String PREFIX_PARAMS = "params.";

	private final StringBuilder sourceBuilder = new StringBuilder();

	private final Map<String, JsonData> params = new HashMap<>();

	private String lang = null;

	// region params
	public <R> ScriptBuilder<T> put(EFunction<T, R> func, R value) {
		Field field = ElasticSearchUtils.resolveField(func);
		return put(field.getName(), value);
	}

	public <R> ScriptBuilder<T> put(String name, R value) {
		JsonData data = JsonData.of(value);
		params.put(name, data);
		return this;
	}

	// endregion
	// region script

	public ScriptBuilder<T> append(String script) {
		if (!StringUtils.hasText(script)) {
			return this;
		}
		sourceBuilder.append(script);
		if (!script.endsWith("}")) {
			sourceBuilder.append(";");
		}
		return this;
	}

	public ScriptBuilder<T> append(String script, String field, Object value) {
		params.put(field, JsonData.of(value));
		return append(script);
	}

	public <R> ScriptBuilder<T> set(EFunction<T, R> func, R value) {
		Field field = ElasticSearchUtils.resolveField(func);
		return set(field.getName(), value);
	}

	public ScriptBuilder<T> set(String field, Object value) {
		if (value == null) {
			String source = String.format("%s%s = null", PREFIX_SOURCE, field);
			return append(source);
		}

		String source = String.format("%s%s = %s%s", PREFIX_SOURCE, field, PREFIX_PARAMS, field);
		params.put(field, JsonData.of(value));
		return append(source);
	}

	public ScriptBuilder<T> lang(String lang) {
		this.lang = lang;
		return this;
	}

	public ScriptBuilder<T> painless() {
		return lang("painless");
	}
	// endregion
	// region build

	public InlineScript inlineScript() {
		String source = sourceBuilder.toString();
		return InlineScript.of(inline -> inline.source(source).lang(lang).params(params));
	}

	public Script inline() {
		return Script.of(script -> script.inline(inlineScript()));
	}

	// endregion

}
