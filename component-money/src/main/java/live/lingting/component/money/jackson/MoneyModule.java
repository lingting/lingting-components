package live.lingting.component.money.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import live.lingting.component.core.util.StringUtils;
import live.lingting.component.money.Money;

import java.io.IOException;

/**
 * @author lingting 2023-05-07 18:46
 */
public class MoneyModule extends SimpleModule {

	public MoneyModule() {
		register();
	}

	protected void register() {
		addSerializer(Money.class, new MoneySerializer());
		addDeserializer(Money.class, new MoneyDeserializer());
	}

	public static class MoneySerializer extends JsonSerializer<Money> {

		@Override
		public void serialize(Money value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			// 使用原始字符串
			String jsonValue = value.toRawString();
			gen.writeString(jsonValue);
		}

	}

	public static class MoneyDeserializer extends JsonDeserializer<Money> {

		@Override
		public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
			String text = p.getText();
			if (!StringUtils.hasText(text)) {
				return null;
			}
			return Money.of(text);
		}

	}

}
