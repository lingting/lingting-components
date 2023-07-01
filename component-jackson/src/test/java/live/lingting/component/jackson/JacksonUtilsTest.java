package live.lingting.component.jackson;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.jackson.sensitive.Sensitive;
import live.lingting.component.jackson.sensitive.SensitiveType;
import lombok.Data;
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

	@Test
	void sensitive() {
		SensitiveTest test = new SensitiveTest();
		test.setAll("all");
		test.setDefaultValue("default");
		test.setMobile("8677711113333");
		String json = JacksonUtils.toJson(test);
		System.out.println(json);
		Assertions.assertTrue(json.contains("\"******\""));
		Assertions.assertTrue(json.contains("d******t"));
		Assertions.assertTrue(json.contains("86******33"));
	}

	@Data
	static class SensitiveTest {

		@Sensitive(SensitiveType.ALL)
		private String all;

		@Sensitive(SensitiveType.DEFAULT)
		private String defaultValue;

		@Sensitive(SensitiveType.MOBILE)
		private String mobile;

	}

}
