package live.lingting.component.easyexcel.configuration;

import live.lingting.component.core.util.CollectionUtils;
import live.lingting.component.easyexcel.aop.RequestExcelArgumentResolver;
import live.lingting.component.easyexcel.aop.ResponseExcelReturnValueHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2023-12-21 16:07
 */
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnClass(RequestMappingHandlerAdapter.class)
public class ComponentEasyExcelSpringMvcAutoConfiguration implements InitializingBean {

	private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

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
