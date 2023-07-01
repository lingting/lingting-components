package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * @author lingting 2023-05-15 10:51
 */
public abstract class AbstractSensitiveSerializer extends JsonSerializer<String> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected Sensitive sensitive;

	@Override
	public void serialize(String raw, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		if (raw == null) {
			raw = "";
		}
		String val = serialize(raw, sensitive);
		gen.writeString(val);
	}

	/**
	 * 传入原始值
	 * @return 返回脱敏后的值
	 */
	public abstract String serialize(String raw, Sensitive sensitive);

}
