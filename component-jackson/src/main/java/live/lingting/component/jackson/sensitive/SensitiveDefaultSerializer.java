package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import live.lingting.component.core.util.SpringUtils;

import java.io.IOException;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveDefaultSerializer extends AbstractSensitiveSerializer implements ContextualSerializer {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String raw = value.toString();
		String val = SensitiveUtils.serialize(raw, 1, 1, sensitive);
		gen.writeString(val);
	}

	@Override
	public JsonSerializer<Object> of(Sensitive sensitive) {
		SensitiveDefaultSerializer serializer = new SensitiveDefaultSerializer();
		serializer.sensitive = sensitive;
		return serializer;
	}

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		Sensitive sensitive = property.getAnnotation(Sensitive.class);
		if (sensitive == null) {
			return prov.findValueSerializer(property.getType(), property);
		}

		AbstractSensitiveSerializer serializer;
		switch (sensitive.type()) {
			case DEFAULT:
				serializer = this;
				break;
			case ALL:
				serializer = SpringUtils.getBean(SensitiveAllSerializer.class);
				break;
			case MOBILE:
				serializer = SpringUtils.getBean(SensitiveMobileSerializer.class);
				break;
			default:
				serializer = SpringUtils.getBean(sensitive.cls());
				break;
		}
		return serializer.of(sensitive);
	}

}
