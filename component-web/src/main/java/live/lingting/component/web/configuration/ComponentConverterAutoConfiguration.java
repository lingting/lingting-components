package live.lingting.component.web.configuration;

import live.lingting.component.web.converter.Converter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.core.convert.converter.ConverterRegistry;

import java.util.List;

/**
 * @author lingting 2023-04-28 16:31
 */
@AutoConfiguration
public class ComponentConverterAutoConfiguration {

	public ComponentConverterAutoConfiguration(ConverterRegistry registry, List<Converter> converters) {
		for (Converter converter : converters) {
			registry.addConverter(converter);
		}
	}

}
