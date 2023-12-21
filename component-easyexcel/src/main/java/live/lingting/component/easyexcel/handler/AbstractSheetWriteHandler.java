package live.lingting.component.easyexcel.handler;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import live.lingting.component.easyexcel.annotation.ResponseExcel;
import live.lingting.component.easyexcel.aop.DynamicNameAspect;
import live.lingting.component.easyexcel.domain.SheetBuildProperties;
import live.lingting.component.easyexcel.enhance.WriterBuilderEnhancer;
import live.lingting.component.easyexcel.head.HeadGenerator;
import live.lingting.component.easyexcel.head.HeadMeta;
import live.lingting.component.easyexcel.head.I18nHeaderCellWriteHandler;
import live.lingting.component.easyexcel.kit.EasyExcelProvider;
import live.lingting.component.easyexcel.kit.ExcelException;
import live.lingting.component.easyexcel.properties.ExcelConfigProperties;
import live.lingting.component.spring.util.SpringUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * @author lengleng
 * @author L.cm
 * @author Hccake
 * @author lingting
 */
@RequiredArgsConstructor
public abstract class AbstractSheetWriteHandler implements SheetWriteHandler {

	private final ExcelConfigProperties configProperties;

	private final EasyExcelProvider provider;

	private final WriterBuilderEnhancer excelWriterBuilderEnhance;

	@Getter
	@Setter
	@Autowired(required = false)
	private I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler;

	@Override
	public void check(ResponseExcel responseExcel) {
		if (responseExcel.fill() && !StringUtils.hasText(responseExcel.template())) {
			throw new ExcelException("@ResponseExcel fill 必须配合 template 使用");
		}
	}

	@SneakyThrows
	@Override
	public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
		check(responseExcel);
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		String name = (String) Objects.requireNonNull(requestAttributes)
			.getAttribute(DynamicNameAspect.EXCEL_NAME_KEY, RequestAttributes.SCOPE_REQUEST);
		if (name == null) {
			name = UUID.randomUUID().toString();
		}
		String fileName = String.format("%s%s", URLEncoder.encode(name, "utf-8"), responseExcel.suffix().getValue())
			.replace("+", "%20");
		// 根据实际的文件类型找到对应的 contentType
		String contentType = MediaTypeFactory.getMediaType(fileName)
			.map(MediaType::toString)
			.orElse("application/vnd.ms-excel");
		response.setContentType(contentType);
		response.setCharacterEncoding("utf-8");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
		write(o, response, responseExcel);
	}

	/**
	 * 通用的获取ExcelWriter方法
	 * @param response HttpServletResponse
	 * @param responseExcel ResponseExcel注解
	 * @return ExcelWriter
	 */
	@SneakyThrows(IOException.class)
	public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
		ExcelWriterBuilder writerBuilder = provider.writer(response.getOutputStream())
			.registerWriteHandler(CustomCellWriteHandler.INSTANCE)
			.excelType(responseExcel.suffix())
			.inMemory(responseExcel.inMemory());

		if (StringUtils.hasText(responseExcel.password())) {
			writerBuilder.password(responseExcel.password());
		}

		if (responseExcel.include().length != 0) {
			writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
		}

		if (responseExcel.exclude().length != 0) {
			writerBuilder.excludeColumnFieldNames(Arrays.asList(responseExcel.exclude()));
		}

		for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
			writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
		}

		// 开启国际化头信息处理
		if (responseExcel.i18nHeader() && i18nHeaderCellWriteHandler != null) {
			writerBuilder.registerWriteHandler(i18nHeaderCellWriteHandler);
		}

		for (Class<? extends Converter> clazz : responseExcel.converter()) {
			writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
		}

		String templatePath = configProperties.getTemplatePath();
		if (StringUtils.hasText(responseExcel.template())) {
			ClassPathResource classPathResource = new ClassPathResource(
					templatePath + File.separator + responseExcel.template());
			InputStream inputStream = classPathResource.getInputStream();
			writerBuilder.withTemplate(inputStream);
		}

		writerBuilder = excelWriterBuilderEnhance.enhanceExcel(writerBuilder, response, responseExcel, templatePath);
		return writerBuilder.build();
	}

	/**
	 * 构建一个 空的 WriteSheet 对象
	 * @param sheetBuildProperties sheet build 属性
	 * @param template 模板信息
	 * @return WriteSheet
	 */
	public WriteSheet emptySheet(SheetBuildProperties sheetBuildProperties, String template) {
		// Sheet 编号和名称
		Integer sheetNo = sheetBuildProperties.getSheetNo() >= 0 ? sheetBuildProperties.getSheetNo() : null;
		String sheetName = sheetBuildProperties.getSheetName();

		// 是否模板写入
		ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template)
				? EasyExcelFactory.writerSheet(sheetNo) : EasyExcelFactory.writerSheet(sheetNo, sheetName);

		return writerSheetBuilder.build();
	}

	/**
	 * 获取 WriteSheet 对象
	 * @param sheetBuildProperties sheet annotation info
	 * @param dataClass 数据类型
	 * @param template 模板
	 * @param bookHeadEnhancerClass 自定义头处理器
	 * @return WriteSheet
	 */
	public WriteSheet emptySheet(SheetBuildProperties sheetBuildProperties, Class<?> dataClass, String template,
			Class<? extends HeadGenerator> bookHeadEnhancerClass) {

		// Sheet 编号和名称
		Integer sheetNo = sheetBuildProperties.getSheetNo() >= 0 ? sheetBuildProperties.getSheetNo() : null;
		String sheetName = sheetBuildProperties.getSheetName();

		// 是否模板写入
		ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template)
				? EasyExcelFactory.writerSheet(sheetNo) : EasyExcelFactory.writerSheet(sheetNo, sheetName);

		// 头信息增强 1. 优先使用 sheet 指定的头信息增强 2. 其次使用 @ResponseExcel 中定义的全局头信息增强
		Class<? extends HeadGenerator> headGenerateClass = null;
		if (isNotInterface(sheetBuildProperties.getHeadGenerateClass())) {
			headGenerateClass = sheetBuildProperties.getHeadGenerateClass();
		}
		else if (isNotInterface(bookHeadEnhancerClass)) {
			headGenerateClass = bookHeadEnhancerClass;
		}
		// 定义头信息增强则使用其生成头信息，否则使用 dataClass 来自动获取
		if (headGenerateClass != null) {
			fillCustomHeadInfo(dataClass, bookHeadEnhancerClass, writerSheetBuilder);
		}
		else if (dataClass != null) {
			writerSheetBuilder.head(dataClass);
			if (sheetBuildProperties.getExcludes().length > 0) {
				writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheetBuildProperties.getExcludes()));
			}
			if (sheetBuildProperties.getIncludes().length > 0) {
				writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheetBuildProperties.getIncludes()));
			}
		}

		// sheetBuilder 增强
		writerSheetBuilder = excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName, dataClass,
				template, headGenerateClass);

		return writerSheetBuilder.build();
	}

	private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass,
			ExcelWriterSheetBuilder writerSheetBuilder) {
		HeadGenerator headGenerator = SpringUtils.getBean(headEnhancerClass);
		Assert.notNull(headGenerator, "The header generated bean does not exist.");
		HeadMeta head = headGenerator.head(dataClass);
		writerSheetBuilder.head(head.getHead());
		writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
	}

	/**
	 * 是否为Null Head Generator
	 * @param headGeneratorClass 头生成器类型
	 * @return true 已指定 false 未指定(默认值)
	 */
	private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
		return !Modifier.isInterface(headGeneratorClass.getModifiers());
	}

}
