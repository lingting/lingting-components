package live.lingting.component.easyexcel.configuration;

import live.lingting.component.easyexcel.aop.ResponseExcelReturnValueHandler;
import live.lingting.component.easyexcel.enhance.DefaultWriterBuilderEnhancer;
import live.lingting.component.easyexcel.enhance.WriterBuilderEnhancer;
import live.lingting.component.easyexcel.handler.ManySheetWriteHandler;
import live.lingting.component.easyexcel.handler.SheetWriteHandler;
import live.lingting.component.easyexcel.handler.SingleSheetWriteHandler;
import live.lingting.component.easyexcel.head.I18nHeaderCellWriteHandler;
import live.lingting.component.easyexcel.kit.EasyExcelProvider;
import live.lingting.component.easyexcel.properties.ExcelConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author Hccake 2020/10/28
 * @version 1.0
 */
@AutoConfiguration
@RequiredArgsConstructor
public class ComponentEasyExcelHandlerAutoConfiguration {

	private final ExcelConfigProperties configProperties;

	/**
	 * ExcelBuild增强
	 * @return DefaultWriterBuilderEnhancer 默认什么也不做的增强器
	 */
	@Bean
	@ConditionalOnMissingBean
	public WriterBuilderEnhancer writerBuilderEnhancer() {
		return new DefaultWriterBuilderEnhancer();
	}

	/**
	 * 单sheet 写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public SingleSheetWriteHandler singleSheetWriteHandler(EasyExcelProvider provider) {
		return new SingleSheetWriteHandler(configProperties, provider, writerBuilderEnhancer());
	}

	/**
	 * 多sheet 写入处理器
	 */
	@Bean
	@ConditionalOnMissingBean
	public ManySheetWriteHandler manySheetWriteHandler(EasyExcelProvider provider) {
		return new ManySheetWriteHandler(configProperties, provider, writerBuilderEnhancer());
	}

	/**
	 * 返回Excel文件的 response 处理器
	 * @param sheetWriteHandlerList 页签写入处理器集合
	 * @return ResponseExcelReturnValueHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(
			List<SheetWriteHandler> sheetWriteHandlerList) {
		return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
	}

	/**
	 * excel 头的国际化处理器
	 * @param messageSource 国际化源
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(MessageSource.class)
	public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler(MessageSource messageSource) {
		return new I18nHeaderCellWriteHandler(messageSource);
	}

}
