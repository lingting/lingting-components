package live.lingting.component.easyexcel.converters;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import live.lingting.component.core.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lingting 2023-03-27 19:54
 */
public enum ListStringConverter implements Converter<List<?>> {

	/**
	 * 实例
	 */
	INSTANCE;

	@Override
	public Class<?> supportJavaTypeKey() {
		return List.class;
	}

	@Override
	public CellDataTypeEnum supportExcelTypeKey() {
		return CellDataTypeEnum.STRING;
	}

	@Override
	public WriteCellData<?> convertToExcelData(List<?> value, ExcelContentProperty contentProperty,
			GlobalConfiguration globalConfiguration) throws Exception {
		if (CollectionUtils.isEmpty(value)) {
			return new WriteCellData<>("");
		}
		return new WriteCellData<>(value.stream().map(Convert::toStr).collect(Collectors.joining(", ")));
	}

}
