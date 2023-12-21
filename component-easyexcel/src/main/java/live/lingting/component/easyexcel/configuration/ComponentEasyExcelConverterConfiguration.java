package live.lingting.component.easyexcel.configuration;

import live.lingting.component.easyexcel.converters.BooleanConverter;
import live.lingting.component.easyexcel.converters.EnumConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-12-21 15:06
 */
@AutoConfiguration
public class ComponentEasyExcelConverterConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public BooleanConverter.OfBoolean booleanConverterOfBoolean() {
		return BooleanConverter.OfBoolean.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean
	public BooleanConverter.OfString booleanConverterOfString() {
		return BooleanConverter.OfString.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean
	public BooleanConverter.OfNumber booleanConverterOfNumber() {
		return BooleanConverter.OfNumber.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean
	public EnumConverter.OfString enumConverterOfString() {
		return EnumConverter.OfString.INSTANCE;
	}

	@Bean
	@ConditionalOnMissingBean
	public EnumConverter.OfNumber enumConverterOfNumber() {
		return EnumConverter.OfNumber.INSTANCE;
	}

}
