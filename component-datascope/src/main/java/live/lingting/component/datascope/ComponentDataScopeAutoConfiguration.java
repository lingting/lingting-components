package live.lingting.component.datascope;

import live.lingting.component.datascope.handler.DefaultDataPermissionHandler;
import live.lingting.component.datascope.interceptor.DataPermissionAnnotationAdvisor;
import live.lingting.component.datascope.interceptor.DataPermissionInterceptor;
import live.lingting.component.datascope.processor.DataScopeSqlProcessor;
import live.lingting.component.datascope.handler.DataPermissionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author hccake
 */
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnBean(DataScope.class)
public class ComponentDataScopeAutoConfiguration {

	/**
	 * 数据权限处理器
	 * @param dataScopeList 需要控制的数据范围集合
	 * @return DataPermissionHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionHandler dataPermissionHandler(List<DataScope> dataScopeList) {
		return new DefaultDataPermissionHandler(dataScopeList);
	}

	/**
	 * 数据权限注解 Advisor，用于处理数据权限的链式调用关系
	 * @return DataPermissionAnnotationAdvisor
	 */
	@Bean
	@ConditionalOnMissingBean(DataPermissionAnnotationAdvisor.class)
	public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
		return new DataPermissionAnnotationAdvisor();
	}

	/**
	 * mybatis 拦截器，用于拦截处理 sql
	 * @param dataPermissionHandler 数据权限处理器
	 * @return DataPermissionInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionHandler dataPermissionHandler) {
		return new DataPermissionInterceptor(new DataScopeSqlProcessor(), dataPermissionHandler);
	}

}
