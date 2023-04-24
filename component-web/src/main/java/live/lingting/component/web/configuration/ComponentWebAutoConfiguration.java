package live.lingting.component.web.configuration;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import live.lingting.component.web.argumentresolve.PageLimitArgumentResolve;
import live.lingting.component.web.filter.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lingting 2022/12/1 10:31
 */
@AutoConfiguration
public class ComponentWebAutoConfiguration {

	@Bean
	public FilterRegistrationBean<TraceIdFilter> traceIdFilterFilterRegistrationBean() {
		TraceIdFilter filter = new TraceIdFilter();
		FilterRegistrationBean<TraceIdFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}

	@Bean
	public WebMvcConfigurer componentWebMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
				resolvers.add(new PageLimitArgumentResolve());
			}
		};
	}

	/**
	 * 自定义async线程池
	 */
	@Bean
	public TaskExecutorCustomizer votesTaskexecutorcustomizer() {
		return taskExecutor -> {
			taskExecutor.setThreadNamePrefix("Async-");
			taskExecutor.setMaxPoolSize(100000);
			taskExecutor.setCorePoolSize(200);
			taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public WebServerFactoryCustomizer<UndertowServletWebServerFactory> undertowServletWebServerFactoryCustomization() {
		return factory -> factory.addDeploymentInfoCustomizers(deploymentInfo -> {
			WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
			webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 2048, -1, 24, 0));
			deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo",
					webSocketDeploymentInfo);
		});
	}

}
