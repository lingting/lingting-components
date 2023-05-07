package live.lingting.component.money.configuration;

import live.lingting.component.money.web.MoneyConverter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.ConverterRegistry;

/**
 * @author lingting 2023-05-07 18:40
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class MoneyWebAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MoneyConverter moneyConverter() {
		return new MoneyConverter();
	}

	@Configuration
	@ConditionalOnBean({ MoneyConverter.class })
	public static class MoneyWebRegistry {

		public MoneyWebRegistry(ConverterRegistry registry, MoneyConverter converter) {
			registry.addConverter(converter);
		}

	}

}
