package live.lingting.component.money.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.lingting.component.money.jackson.MoneyModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-05-07 18:45
 */
@AutoConfiguration
@ConditionalOnClass(ObjectMapper.class)
public class MoneyJacksonAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MoneyModule moneyModule() {
		return new MoneyModule();
	}

}
