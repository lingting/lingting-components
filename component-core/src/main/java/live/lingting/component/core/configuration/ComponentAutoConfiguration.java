package live.lingting.component.core.configuration;

import live.lingting.component.core.spring.post.ContextComposeBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-24 21:32
 */
@AutoConfiguration
public class ComponentAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ContextComposeBeanPostProcessor contextComposeBeanPostProcessor() {
		return new ContextComposeBeanPostProcessor();
	}

}
