package live.lingting.component.spring.configuration;

import live.lingting.component.spring.mdc.MdcTaskDecorator;
import live.lingting.component.spring.post.ContextComposeBeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;

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

	@Bean
	@ConditionalOnMissingBean
	public TaskDecorator taskDecorator() {
		return new MdcTaskDecorator();
	}

}
