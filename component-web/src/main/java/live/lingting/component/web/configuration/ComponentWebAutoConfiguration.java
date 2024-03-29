package live.lingting.component.web.configuration;

import live.lingting.component.web.argumentresolve.PageLimitArgumentResolve;
import live.lingting.component.web.filter.TraceIdFilter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2022/12/1 10:31
 */
@AutoConfiguration
@ComponentScan("live.lingting.component.web.exception")
public class ComponentWebAutoConfiguration {

	@Bean
	@ConditionalOnMissingFilterBean
	public FilterRegistrationBean<TraceIdFilter> traceIdFilterFilterRegistrationBean() {
		TraceIdFilter filter = new TraceIdFilter();
		FilterRegistrationBean<TraceIdFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	/**
	 * 自定义async线程池
	 */
	@Bean
	@ConditionalOnMissingBean
	public TaskExecutorCustomizer taskexecutorcustomizer(ObjectProvider<TaskDecorator> provider) {

		return taskExecutor -> {
			provider.ifAvailable(taskExecutor::setTaskDecorator);

			taskExecutor.setThreadNamePrefix("Async-");
			taskExecutor.setMaxPoolSize(100000);
			taskExecutor.setCorePoolSize(200);

			taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		};
	}

	// region ArgumentResolve

	@Bean
	@ConditionalOnMissingBean
	public PageLimitArgumentResolve pageLimitArgumentResolve() {
		return new PageLimitArgumentResolve();
	}

	@Bean
	public WebMvcConfigurer componentWebMvcConfigurer(PageLimitArgumentResolve resolve) {
		return new WebMvcConfigurer() {
			@Override
			public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
				resolvers.add(resolve);
			}
		};
	}

	// endregion

}
