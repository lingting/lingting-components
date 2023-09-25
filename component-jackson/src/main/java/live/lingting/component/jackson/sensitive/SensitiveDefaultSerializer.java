package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import live.lingting.component.core.constant.GlobalConstants;
import live.lingting.component.spring.util.SpringUtils;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveDefaultSerializer extends AbstractSensitiveSerializer implements ContextualSerializer {

	@Override
	public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
			throws JsonMappingException {
		Sensitive sensitive = property.getAnnotation(Sensitive.class);
		if (sensitive == null) {
			return prov.findValueSerializer(property.getType(), property);
		}

		SensitiveDefaultSerializer serializer = new SensitiveDefaultSerializer();
		serializer.sensitive = sensitive;
		return serializer;
	}

	@Override
	public String serialize(String raw, Sensitive sensitive) {
		switch (sensitive.value()) {
			case ALL:
				return sensitive.middle();
			case DEFAULT:
				return SensitiveUtils.serialize(raw, 1, 1, sensitive);
			case MOBILE:
				if (raw.startsWith(GlobalConstants.PLUS)) {
					String serialize = SensitiveUtils.serialize(raw.substring(1), 2, 2, sensitive);
					return GlobalConstants.PLUS + serialize;
				}
				return SensitiveUtils.serialize(raw, 2, 2, sensitive);
			case CUSTOMER:
				AbstractSensitiveSerializer bean = SpringUtils.getBean(sensitive.cls());
				return bean.serialize(raw, sensitive);
			default:
				return raw;
		}
	}

}
