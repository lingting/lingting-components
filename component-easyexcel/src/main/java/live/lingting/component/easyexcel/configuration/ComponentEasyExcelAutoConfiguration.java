package live.lingting.component.easyexcel.configuration;

import com.alibaba.excel.converters.Converter;
import live.lingting.component.easyexcel.aop.DynamicNameAspect;
import live.lingting.component.easyexcel.head.EmptyHeadGenerator;
import live.lingting.component.easyexcel.kit.EasyExcelProvider;
import live.lingting.component.easyexcel.processor.NameProcessor;
import live.lingting.component.easyexcel.processor.NameSpelExpressionProcessor;
import live.lingting.component.easyexcel.properties.ExcelConfigProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lengleng
 * <p>
 * 配置初始化
 */
@AutoConfiguration
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ComponentEasyExcelAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public EasyExcelProvider easyExcelProvider(List<Converter<?>> converters) {
		return new EasyExcelProvider(converters);
	}

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

}
