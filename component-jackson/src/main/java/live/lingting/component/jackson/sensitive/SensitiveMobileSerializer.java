package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import live.lingting.component.core.constant.GlobalConstants;

import java.io.IOException;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveMobileSerializer extends AbstractSensitiveSerializer {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String raw = value.toString();
		String val;
		if (raw.startsWith(GlobalConstants.PLUS)) {
			String serialize = SensitiveUtils.serialize(raw.substring(1), 2, 2, sensitive);
			val = GlobalConstants.PLUS + serialize;
		}
		else {
			val = SensitiveUtils.serialize(raw, 2, 2, sensitive);
		}
		gen.writeString(val);
	}

	@Override
	public JsonSerializer<Object> of(Sensitive sensitive) {
		SensitiveMobileSerializer serializer = new SensitiveMobileSerializer();
		serializer.sensitive = sensitive;
		return serializer;
	}

}
