package live.lingting.component.jackson.module;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import live.lingting.component.core.util.ArrayUtils;
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

		private static final String[] STR_TRUE = { "1", "true", "yes", "ok", "y" };

		private static final String[] STR_FALSE = { "0", "false", "no", "n" };

		@Override
		public Boolean deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
				throws IOException {
			String text = jsonParser.getText();
			if (!StringUtils.hasText(text)) {
				throw new JsonParseException(jsonParser, String.format("不支持文本[%s]转布尔值!", text));
			}
			text = text.trim().toLowerCase();
			if (ArrayUtils.contains(STR_TRUE, text)) {
				return true;
			}
			if (ArrayUtils.contains(STR_FALSE, text)) {
				return false;
			}

			try {
				Number number = jsonParser.getNumberValue();
				if (number == null) {
					throw new JsonParseException(jsonParser, "不支持数值 Null 转布尔值!");
				}
				return number.doubleValue() > 0;
			}
			catch (Exception e) {
				//
			}

			return jsonParser.getBooleanValue();
		}

	}

}
