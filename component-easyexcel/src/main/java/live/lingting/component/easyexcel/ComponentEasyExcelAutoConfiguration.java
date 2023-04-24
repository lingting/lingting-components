package live.lingting.component.easyexcel;

import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.easyexcel.aop.DynamicNameAspect;
import live.lingting.component.easyexcel.aop.RequestExcelArgumentResolver;
import live.lingting.component.easyexcel.aop.ResponseExcelReturnValueHandler;
import live.lingting.component.easyexcel.properties.ExcelConfigProperties;
import live.lingting.component.easyexcel.head.EmptyHeadGenerator;
import live.lingting.component.easyexcel.processor.NameProcessor;
import live.lingting.component.easyexcel.processor.NameSpelExpressionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * <p>
 * 配置初始化
 */
@AutoConfiguration
@Import(ComponentEasyExcelHandlerConfiguration.class)
@RequiredArgsConstructor
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ComponentEasyExcelAutoConfiguration implements InitializingBean {

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

	/**
	 * SPEL 解析处理器
	 * @return NameProcessor excel名称解析器
	 */
	@Bean
	@ConditionalOnMissingBean
	public NameProcessor nameProcessor() {
		return new NameSpelExpressionProcessor();
	}

	/**
	 * Excel名称解析处理切面
	 * @param nameProcessor SPEL 解析处理器
	 * @return DynamicNameAspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
		return new DynamicNameAspect(nameProcessor);
	}

	/**
	 * 空的 Excel 头生成器
	 * @return EmptyHeadGenerator
	 */
	@Bean
	@ConditionalOnMissingBean
	public EmptyHeadGenerator emptyHeadGenerator() {
		return new EmptyHeadGenerator();
	}

	/**
	 * 追加 Excel返回值处理器 到 springmvc 中
	 */
	public void setReturnValueHandlers() {
		List<HandlerMethodReturnValueHandler> returnValueHandlers = requestMappingHandlerAdapter
			.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(responseExcelReturnValueHandler);
		if (!CollectionUtils.isEmpty(returnValueHandlers)) {
			newHandlers.addAll(returnValueHandlers);
		}
		requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
	}

	/**
	 * 追加 Excel 请求处理器 到 springmvc 中
	 */
	public void setRequestExcelArgumentResolver() {
		List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
		List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
		resolverList.add(new RequestExcelArgumentResolver());
		if (!CollectionUtils.isEmpty(argumentResolvers)) {
			resolverList.addAll(argumentResolvers);
		}
		requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		setReturnValueHandlers();
		setRequestExcelArgumentResolver();
	}

}
