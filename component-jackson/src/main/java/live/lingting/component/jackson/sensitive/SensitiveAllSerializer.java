package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * @author lingting 2023-04-27 15:30
 */
public class SensitiveAllSerializer extends AbstractSensitiveSerializer {

	@Override
	public String serialize(String raw) {
		return SensitiveUtils.MIDDLE;
	}

	@Override
	public JsonSerializer<Object> of(Sensitive sensitive) {
		return this;
	}

}
