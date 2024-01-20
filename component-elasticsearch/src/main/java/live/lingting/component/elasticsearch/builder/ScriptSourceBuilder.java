package live.lingting.component.elasticsearch.builder;

/**
 * @author lingting 2024-01-20 14:11
 */
public class ScriptSourceBuilder {

	public static final String PREFIX_SOURCE = "ctx._source";

	public static final String PREFIX_PARAMS = "params";

	private String prefixSource = PREFIX_SOURCE;

	private String prefixParams = PREFIX_PARAMS;

	private String field;

	public ScriptSourceBuilder prefixSource(String prefixSource) {
		this.prefixSource = prefixSource;
		return this;
	}

	public ScriptSourceBuilder prefixParams(String prefixParams) {
		this.prefixParams = prefixParams;
		return this;
	}

	public ScriptSourceBuilder field(String field) {
		this.field = field;
		return this;
	}

	protected String sourceField() {
		return String.format("%s.%s", prefixSource, field);
	}

	protected String paramsField() {
		return String.format("%s.%s", prefixParams, field);
	}

	public String setNull() {
		return String.format("%s = null", sourceField());
	}

	public String setParams() {
		return String.format("%s = %s", sourceField(), paramsField());
	}

	public String setIfAbsent() {
		return String.format("if(%s==null || %s==''){%s=%s;}", sourceField(), sourceField(), sourceField(),
				paramsField());
	}

}
