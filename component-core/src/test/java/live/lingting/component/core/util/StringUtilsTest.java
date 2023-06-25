package live.lingting.component.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-06-25 16:56
 */
class StringUtilsTest {

	@Test
	void underscoreToHump() {
		String raw = "contact_id";
		String hump = StringUtils.underscoreToHump(raw);
		Assertions.assertEquals("contactId", hump);
	}

}
