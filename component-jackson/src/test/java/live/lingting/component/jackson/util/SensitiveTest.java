package live.lingting.component.jackson.util;

import live.lingting.component.jackson.JacksonUtils;
import live.lingting.component.jackson.sensitive.Sensitive;
import live.lingting.component.jackson.sensitive.SensitiveType;
import live.lingting.component.jackson.sensitive.SensitiveUtils;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-04-27 15:53
 */
class SensitiveTest {

	@Test
	void serialize() {
		String raw = "这是一个要脱敏的文本";
		String r1 = SensitiveUtils.serialize(raw, 1, 1);
		System.out.println(r1);
		Assertions.assertEquals("这******本", r1);
		String r2 = SensitiveUtils.serialize(raw, 2, 2);
		System.out.println(r2);
		Assertions.assertEquals("这是******文本", r2);
		String r12 = SensitiveUtils.serialize(raw, 1, 2);
		System.out.println(r12);
		Assertions.assertEquals("这******文本", r12);
		String r21 = SensitiveUtils.serialize(raw, 2, 1);
		System.out.println(r21);
		Assertions.assertEquals("这是******本", r21);
	}

	@Test
	void sensitive() {
		SensitiveTestEntity test = new SensitiveTestEntity();
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
	static class SensitiveTestEntity {

		@Sensitive(SensitiveType.ALL)
		private String all;

		@Sensitive(SensitiveType.DEFAULT)
		private String defaultValue;

		@Sensitive(SensitiveType.MOBILE)
		private String mobile;

	}

}
