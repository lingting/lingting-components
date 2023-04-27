package live.lingting.component.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import live.lingting.component.jackson.util.SensitiveUtils;

import java.io.IOException;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveAllSerializer extends JsonSerializer<Object> {

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeString(SensitiveUtils.MIDDLE);
	}

}
