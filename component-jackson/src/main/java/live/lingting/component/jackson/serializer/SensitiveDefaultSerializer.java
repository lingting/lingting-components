package live.lingting.component.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import live.lingting.component.core.util.SpringUtils;
import live.lingting.component.jackson.annotation.Sensitive;
import live.lingting.component.jackson.util.SensitiveUtils;

import java.io.IOException;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveDefaultSerializer extends JsonSerializer<Object> implements ContextualSerializer {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String raw = value.toString();
		String val = SensitiveUtils.serialize(raw, 1, 1);
		gen.writeString(val);
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		Sensitive sensitive = property.getAnnotation(Sensitive.class);
		if (sensitive == null) {
			return prov.findValueSerializer(property.getType(), property);
		}

		switch (sensitive.type()) {
			case DEFAULT:
				return this;
			case ALL:
				return SpringUtils.getBean(SensitiveAllSerializer.class);
			case MOBILE:
				return SpringUtils.getBean(SensitiveMobileSerializer.class);
			default:
				return SpringUtils.getBean(sensitive.cls());
		}
	}

}
