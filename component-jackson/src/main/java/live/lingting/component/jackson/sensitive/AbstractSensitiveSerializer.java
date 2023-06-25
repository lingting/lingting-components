package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.SneakyThrows;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * @author lingting 2023-05-15 10:51
 */
public abstract class AbstractSensitiveSerializer extends JsonSerializer<Object> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected Sensitive sensitive;

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String raw = value.toString();
		String val = serialize(raw);
		gen.writeString(val);
	}

	/**
	 * 传入原始值
	 * @return 返回脱敏后的值
	 */
	public abstract String serialize(String raw);

	@SneakyThrows
	public JsonSerializer<Object> of(Sensitive sensitive) {
		Class<? extends AbstractSensitiveSerializer> cls = getClass();
		AbstractSensitiveSerializer instance = cls.newInstance();
		instance.sensitive = sensitive;
		return instance;
	}

}
