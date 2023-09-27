package live.lingting.component.jackson;

import live.lingting.component.core.util.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-04-24 19:33
 */
class JacksonUtilsTest {

	@Test
	void test() {
		String json = JacksonUtils.toJson(null);
		Assertions.assertTrue(StringUtils.hasText(json));
	}

}
