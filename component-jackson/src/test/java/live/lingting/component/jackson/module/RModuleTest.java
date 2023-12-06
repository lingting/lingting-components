package live.lingting.component.jackson.module;

import com.fasterxml.jackson.core.type.TypeReference;
import live.lingting.component.core.r.R;
import live.lingting.component.jackson.JacksonUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2023-09-27 11:33
 */
class RModuleTest {

	@Test
	void test() {
		REntity entity = new REntity().setP1("p1").setP2("p2");
		R<REntity> r = R.ok(entity);
		String json = JacksonUtils.toJson(r);
		System.out.println(json);
		Assertions.assertEquals(
				"{\"code\":200,\"data\":{\"p1\":\"p1\",\"p2\":\"p2\"},\"message\":\"Success\",\"ok\":true}", json);
		TypeReference<R<REntity>> reference = new TypeReference<R<REntity>>() {
		};

		R<REntity> o1 = JacksonUtils
			.toObj("{\"code\":200,\"data\":{\"p1\":\"p1\",\"p2\":\"p2\"},\"message\":\"Success\"}", reference);
		System.out.println(o1);
		Assertions.assertEquals(200, o1.getCode());
		Assertions.assertEquals("p1", o1.getData().getP1());

		R<REntity> o2 = JacksonUtils.toObj("{\"code\":200,\"data\": null,\"message\":\"Success\"}", reference);
		System.out.println(o2);
		Assertions.assertEquals(200, o2.getCode());
		Assertions.assertNull(o2.getData());

		R<REntity> o3 = JacksonUtils.toObj("{\"code\":200,\"message\":\"Success\"}", reference);
		System.out.println(o3);
		Assertions.assertEquals(200, o3.getCode());
		Assertions.assertNull(o3.getData());
	}

	@Data
	@Accessors(chain = true)
	static class REntity {

		private String p1;

		private String p2;

	}

}
