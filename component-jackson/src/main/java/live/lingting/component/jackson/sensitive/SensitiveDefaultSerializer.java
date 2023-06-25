package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import live.lingting.component.core.util.SpringUtils;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveDefaultSerializer extends AbstractSensitiveSerializer implements ContextualSerializer {

	@Override
	public String serialize(String raw) {
		return SensitiveUtils.serialize(raw, 1, 1, sensitive);
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
