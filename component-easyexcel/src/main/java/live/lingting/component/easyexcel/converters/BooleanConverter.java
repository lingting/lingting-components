package live.lingting.component.easyexcel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import live.lingting.component.core.util.BooleanUtils;
import live.lingting.component.core.util.StringUtils;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

/**
 * @author lingting 2023-12-21 14:52
 */
@UtilityClass
@SuppressWarnings("java:S6548")
public class BooleanConverter {

	public enum OfBoolean implements Converter<Boolean> {

		INSTANCE;

		@Override
		public Class<?> supportJavaTypeKey() {
			return Boolean.class;
		}

		@Override
		public CellDataTypeEnum supportExcelTypeKey() {
			return CellDataTypeEnum.BOOLEAN;
		}

		@Override
		public Boolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			return cellData.getBooleanValue();
		}

		@Override
		public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			if (value == null) {
				return new WriteCellData<>("");
			}
			return new WriteCellData<>(value);
		}

	}

	public enum OfString implements Converter<Boolean> {

		INSTANCE;

		@Override
		public Class<?> supportJavaTypeKey() {
			return Boolean.class;
		}

		@Override
		public CellDataTypeEnum supportExcelTypeKey() {
			return CellDataTypeEnum.STRING;
		}

		@Override
		public Boolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			String string = cellData.getStringValue();
			if (StringUtils.hasText(string)) {
				return BooleanUtils.isTrue(string);
			}
			return null;
		}

		@Override
		public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			if (value == null) {
				return new WriteCellData<>("");
			}
			return new WriteCellData<>(value);
		}

	}

	public enum OfNumber implements Converter<Boolean> {

		INSTANCE;

		@Override
		public Class<?> supportJavaTypeKey() {
			return Boolean.class;
		}

		@Override
		public CellDataTypeEnum supportExcelTypeKey() {
			return CellDataTypeEnum.NUMBER;
		}

		@Override
		public Boolean convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			BigDecimal decimal = cellData.getNumberValue();
			if (decimal == null) {
				return null;
			}
			// 大于0为true
			return decimal.compareTo(BigDecimal.ZERO) > 0;
		}

		@Override
		public WriteCellData<?> convertToExcelData(Boolean value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			if (value == null) {
				return new WriteCellData<>("");
			}
			return new WriteCellData<>(value);
		}

	}

}
