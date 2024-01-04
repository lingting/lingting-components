package live.lingting.component.web.configuration;

import live.lingting.component.web.converter.Converter;
import live.lingting.component.web.converter.EnumConverter;
import live.lingting.component.web.converter.StringToArrayConverter;
import live.lingting.component.web.converter.StringToCollectionConverter;
import live.lingting.component.web.converter.StringToLocalDateConverter;
import live.lingting.component.web.converter.StringToLocalDateTimeConverter;
import live.lingting.component.web.converter.StringToLocalTimeConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;

import java.util.List;

/**
 * @author lingting 2023-04-28 16:31
 */
@AutoConfiguration
@Import(ComponentConverterAutoConfiguration.ComponentConverterRegistryConfiguration.class)
public class ComponentConverterAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public EnumConverter enumConverter() {
		return new EnumConverter();
	}

	@Bean
	@ConditionalOnMissingBean
	public StringToArrayConverter stringToArrayConverter(ConversionService conversionService) {
		return new StringToArrayConverter(conversionService);
	}

	@Bean
	@ConditionalOnMissingBean
	public StringToCollectionConverter stringToCollectionConverter(ConversionService conversionService) {
		return new StringToCollectionConverter(conversionService);
	}

	@Bean
	@ConditionalOnMissingBean
	public StringToLocalDateConverter stringToLocalDateConverter() {
		return new StringToLocalDateConverter();
	}

	@Bean
	@ConditionalOnMissingBean
	public StringToLocalDateTimeConverter stringToLocalDateTimeConverter() {
		return new StringToLocalDateTimeConverter();
	}

	@Bean
	@ConditionalOnMissingBean
	public StringToLocalTimeConverter stringToLocalTimeConverter() {
		return new StringToLocalTimeConverter();
	}

	public static class ComponentConverterRegistryConfiguration {

		public ComponentConverterRegistryConfiguration(ConverterRegistry registry, List<Converter<?>> converters) {
			for (Converter<?> converter : converters) {
				registry.addConverter(converter);
			}
		}

	}

}
