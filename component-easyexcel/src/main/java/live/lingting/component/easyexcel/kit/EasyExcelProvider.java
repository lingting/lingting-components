package live.lingting.component.easyexcel.kit;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.metadata.AbstractParameterBuilder;
import com.alibaba.excel.metadata.BasicParameter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import live.lingting.component.easyexcel.converters.ListStringConverter;
import live.lingting.component.easyexcel.converters.LocalDateStringConverter;
import live.lingting.component.easyexcel.converters.LocalDateTimeStringConverter;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

/**
 * @author lingting 2023-12-21 15:07
 */
@RequiredArgsConstructor
public class EasyExcelProvider {

	private final List<Converter<?>> converters;

	public ExcelReaderBuilder read() {
		return customer(EasyExcelFactory.read());
	}

	public ExcelReaderBuilder read(InputStream stream) {
		return read().file(stream);
	}

	public ExcelReaderBuilder read(InputStream stream, Class<?> head) {
		return read(stream).head(head);
	}

	public ExcelReaderBuilder read(InputStream stream, ReadListener<?> listener) {
		return read(stream).registerReadListener(listener);
	}

	public ExcelReaderBuilder read(InputStream stream, Class<?> head, ReadListener<?> listener) {
		return read(stream, head).registerReadListener(listener);
	}

	/**
	 * 读取第一个sheet
	 */
	public void readSheet(ExcelReaderBuilder builder) {
		readSheet(builder, Collections.singletonList(new ReadSheet(0)));
	}

	/**
	 * 读取指定sheet
	 */
	public void readSheet(ExcelReaderBuilder builder, List<ReadSheet> sheets) {
		try (ExcelReader reader = builder.build()) {
			reader.read(sheets);
		}
	}

	/**
	 * 读取所有sheet
	 */
	public void readAllSheet(ExcelReaderBuilder builder) {
		try (ExcelReader reader = builder.build()) {
			reader.readAll();
		}
	}

	public ExcelWriterBuilder writer() {
		return customer(EasyExcelFactory.write()).registerConverter(LocalDateStringConverter.INSTANCE)
			.registerConverter(LocalDateTimeStringConverter.INSTANCE)
			.registerConverter(ListStringConverter.INSTANCE)
			.autoCloseStream(true);
	}

	public ExcelWriterBuilder writer(OutputStream stream) {
		return writer().file(stream);
	}

	public ExcelWriterBuilder writer(OutputStream stream, Class<?> head) {
		return writer(stream).head(head);
	}

	public <T extends AbstractParameterBuilder<T, C>, C extends BasicParameter> T customer(T builder) {

		for (Converter<?> converter : converters) {
			builder.registerConverter(converter);
		}

		return builder;
	}

}
