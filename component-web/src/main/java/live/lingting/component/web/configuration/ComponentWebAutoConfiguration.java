package live.lingting.component.web.configuration;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import live.lingting.component.web.argumentresolve.PageLimitArgumentResolve;
import live.lingting.component.web.converter.EnumConverter;
import live.lingting.component.web.converter.StringToArrayConverter;
import live.lingting.component.web.converter.StringToCollectionConverter;
import live.lingting.component.web.converter.StringToLocalDateConverter;
import live.lingting.component.web.converter.StringToLocalDateTimeConverter;
import live.lingting.component.web.converter.StringToLocalTimeConverter;
import live.lingting.component.web.filter.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.core.convert.ConversionService;
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
	public TaskExecutorCustomizer taskexecutorcustomizer() {
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

	// region converter

}
