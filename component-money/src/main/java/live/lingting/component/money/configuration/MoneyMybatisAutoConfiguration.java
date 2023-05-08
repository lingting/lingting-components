package live.lingting.component.money.configuration;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import live.lingting.component.money.mybatis.MoneyTypeHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-05-07 18:27
 */
@AutoConfiguration
@ConditionalOnClass(ConfigurationCustomizer.class)
public class MoneyMybatisAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public MoneyTypeHandler moneyTypeHandler() {
		return new MoneyTypeHandler();
	}

	@Bean
	@ConditionalOnBean(MoneyTypeHandler.class)
	public ConfigurationCustomizer moneyConfigurationCustomizer(MoneyTypeHandler handler) {
		return configuration -> configuration.getTypeHandlerRegistry().register(handler);
	}

}
