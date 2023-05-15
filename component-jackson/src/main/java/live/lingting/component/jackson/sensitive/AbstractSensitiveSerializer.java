package live.lingting.component.jackson.sensitive;

import com.fasterxml.jackson.databind.JsonSerializer;

/**
 * @author lingting 2023-05-15 10:51
 */
public abstract class AbstractSensitiveSerializer extends JsonSerializer<Object> {

	protected Sensitive sensitive;

	public abstract JsonSerializer<Object> of(Sensitive sensitive);

}
