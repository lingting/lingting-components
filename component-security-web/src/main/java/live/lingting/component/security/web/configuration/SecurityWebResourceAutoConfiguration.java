package live.lingting.component.security.web.configuration;

import live.lingting.component.okhttp.OkHttpClientBuilder;
import live.lingting.component.security.authorize.SecurityAuthorize;
import live.lingting.component.security.configuration.SecurityResourceConfiguration;
import live.lingting.component.security.constant.SecurityConstants;
import live.lingting.component.security.properties.SecurityProperties;
import live.lingting.component.security.resource.SecurityResourceService;
import live.lingting.component.security.store.SecurityStore;
import live.lingting.component.security.web.filter.SecurityWebResourceFilter;
import live.lingting.component.security.web.properties.SecurityWebProperties;
import live.lingting.component.security.web.resource.SecurityWebDefaultRemoteResourceServiceImpl;
import live.lingting.component.security.web.resource.SecurityWebRemoteResourceRequestCustomer;
import live.lingting.component.security.web.resource.SecurityWebResourceInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

/**
 * @author lingting 2023-03-29 21:22
 */
@AutoConfiguration
@ConditionalOnBean(SecurityResourceConfiguration.class)
public class SecurityWebResourceAutoConfiguration {

	@Bean
	@ConditionalOnMissingFilterBean
	public FilterRegistrationBean<SecurityWebResourceFilter> securityWebResourceFilterFilterRegistrationBean(
			SecurityResourceService service) {
		SecurityWebResourceFilter filter = new SecurityWebResourceFilter(service);
		FilterRegistrationBean<SecurityWebResourceFilter> bean = new FilterRegistrationBean<>(filter);
		bean.setOrder(SecurityConstants.ORDER_RESOURCE_SCOPE);
		return bean;
	}

	/**
	 * 指定远端解析token
	 * @see SecurityResourceConfiguration#securityStoreResourceService(SecurityStore)
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX + ".authorization", value = "remote",
			havingValue = "true")
	public SecurityResourceService securityRemoteResourceService(SecurityProperties properties,
			SecurityWebRemoteResourceRequestCustomer customer) {
		String host = properties.getAuthorization().getRemoteHost();

		OkHttpClientBuilder builder = new OkHttpClientBuilder().disableSsl()
			.timeout(Duration.ofSeconds(5), Duration.ofSeconds(10));

		return new SecurityWebDefaultRemoteResourceServiceImpl(host, builder.build(), customer);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = SecurityProperties.PREFIX + ".authorization", value = "remote",
			havingValue = "true")
	public SecurityWebRemoteResourceRequestCustomer securityWebRemoteResourceRequestCustomer() {
		return new SecurityWebRemoteResourceRequestCustomer() {
		};
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityWebResourceInterceptor securityWebResourceInterceptor(SecurityWebProperties properties,
			SecurityAuthorize authorize) {
		return new SecurityWebResourceInterceptor(properties, authorize);
	}

	@Bean
	@ConditionalOnBean(SecurityWebResourceInterceptor.class)
	public WebMvcConfigurer securityWebResourceWebMvcConfigurer(SecurityWebResourceInterceptor interceptor) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(interceptor).addPathPatterns("/**");
			}
		};
	}

}
