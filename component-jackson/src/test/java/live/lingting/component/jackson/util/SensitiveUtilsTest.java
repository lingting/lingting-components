package live.lingting.component.jackson.util;

import live.lingting.component.jackson.sensitive.SensitiveUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-04-27 15:53
 */
class SensitiveUtilsTest {

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

}
