package live.lingting.component.jackson.module;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import live.lingting.component.core.util.BooleanUtils;
import live.lingting.component.core.util.StringUtils;

import java.io.IOException;

/**
 * @author lingting 2023-04-18 15:22
 */
public class BooleanModule extends SimpleModule {

	public BooleanModule() {
		super.addDeserializer(Boolean.class, new BooleanDeserializer());
	}

	public static class BooleanDeserializer extends JsonDeserializer<Boolean> {

		@Override
		public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException {
			String text = jsonParser.getText();
			if (!StringUtils.hasText(text)) {
				throw new JsonParseException(jsonParser, String.format("不支持文本[%s]转布尔值!", text));
			}
			text = text.trim().toLowerCase();
			if (BooleanUtils.isTrue(text)) {
				return true;
			}
			if (BooleanUtils.isFalse(text)) {
				return false;
			}

			try {
				Number number = jsonParser.getNumberValue();
				if (number == null) {
					throw new JsonParseException(jsonParser, "不支持数值 Null 转布尔值!");
				}
				return BooleanUtils.isTrue(number);
			}
			catch (JsonParseException e) {
				throw e;
			}
			catch (Exception e) {
				//
			}

			return jsonParser.getBooleanValue();
		}

	}

}
