package live.lingting.component.jackson.spring;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 用于自定义objectMapper
 * @author lingting 2023-04-23 13:18
 */
public interface ObjectMapperCustomizer {

	ObjectMapper apply(ObjectMapper mapper);

}
