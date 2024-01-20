package live.lingting.component.elasticsearch.builder;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author lingting 2024-01-20 14:19
 */
class ScriptSourceBuilderTest {

	@Test
	void test() {
		ScriptSourceBuilder builder = new ScriptSourceBuilder().field("userId");

		assertEquals("ctx._source.userId = null", builder.setNull());
		assertEquals("ctx._source.userId = params.userId", builder.setParams());
		assertEquals("if(ctx._source.userId==null || ctx._source.userId==''){ctx._source.userId=params.userId;}",
				builder.setIfAbsent());
	}

}
