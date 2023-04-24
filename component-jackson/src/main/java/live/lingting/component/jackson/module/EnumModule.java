package live.lingting.component.jackson.module;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.ClassKey;
import live.lingting.component.core.util.EnumUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author lingting 2022/12/20 14:11
 */
@SuppressWarnings("java:S3740")
public class EnumModule extends SimpleModule {

	/**
	 * @author lingting 2022/12/20 14:05
	 */
	public static class EnumJacksonDeserializers extends SimpleDeserializers {

		@Override
		public JsonDeserializer<?> findEnumDeserializer(Class<?> type, DeserializationConfig config,
				BeanDescription beanDesc) throws JsonMappingException {
			if (type.isEnum()) {
				return this._classMappings.get(new ClassKey(Enum.class));
			}
			return super.findEnumDeserializer(type, config, beanDesc);
		}

	}

	public EnumModule() {
		addSerializer(Enum.class, new JsonSerializer<Enum>() {

			@Override
			public void serialize(Enum e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
					throws IOException {
				jsonGenerator.writeObject(EnumUtils.getValue(e));
			}
		});

		EnumJacksonDeserializers deserializers = new EnumJacksonDeserializers();
		deserializers.addDeserializer(Enum.class, new JsonDeserializer<Enum>() {
			@Override
			public Enum deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
					throws IOException {
				// 获取前端输入的原始文本
				String rawString = jsonParser.getValueAsString();

				// 获取上下文
				JsonStreamContext context = jsonParser.getParsingContext();
				// 字段名
				String fieldName = context.getCurrentName();
				// 实体类
				Class<?> aClass = context.getCurrentValue().getClass();

				Field field = ReflectionUtils.findField(aClass, fieldName);

				if (field == null) {
					return null;
				}

				// 获取值
				for (Object obj : field.getType().getEnumConstants()) {
					Enum<?> e = (Enum<?>) obj;
					Object value = EnumUtils.getValue(e);

					if (Objects.equals(value, rawString)
							|| (value != null && Objects.equals(value.toString(), rawString))) {
						return e;
					}
				}

				return null;
			}
		});
		setDeserializers(deserializers);

	}

}
