package live.lingting.component.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import live.lingting.component.core.constant.GlobalConstants;
import live.lingting.component.jackson.util.SensitiveUtils;

import java.io.IOException;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveMobileSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String raw = value.toString();
		String val;
		if (raw.startsWith(GlobalConstants.PLUS)) {
			String serialize = SensitiveUtils.serialize(raw.substring(1), 2, 2);
			val = GlobalConstants.PLUS + serialize;
		}
		else {
			val = SensitiveUtils.serialize(raw, 2, 2);
		}
		gen.writeString(val);
	}

}
