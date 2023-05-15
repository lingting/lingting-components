package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveAllSerializer extends AbstractSensitiveSerializer {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(SensitiveUtils.MIDDLE);
	}

	@Override
	public JsonSerializer<Object> of(Sensitive sensitive) {
		return this;
	}

}
