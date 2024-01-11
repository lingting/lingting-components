package live.lingting.component.easyexcel.converters;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import live.lingting.component.core.util.EnumUtils;
import live.lingting.component.easyexcel.kit.EasyExcelEnum;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * @author lingting 2023-12-21 14:57
 */
@UtilityClass
@SuppressWarnings({ "java:S1452", "java:S6548" })
public class EnumConverter {

	public static WriteCellData<Object> toCellData(Enum<?> value) {
		if (value == null) {
			return new WriteCellData<>("");
		}

		if (value instanceof EasyExcelEnum) {
			return new WriteCellData<>(((EasyExcelEnum) value).getExcelValue());
		}

		Object ev = EnumUtils.getValue(value);
		return new WriteCellData<>(ev == null ? "" : ev.toString());
	}

	public static Enum<?> of(BigDecimal decimal, ExcelContentProperty property) {
		return of(decimal == null ? null : decimal.stripTrailingZeros().toPlainString(), property);
	}

	public static Enum<?> of(String string, ExcelContentProperty property) {
		if (string == null) {
			return null;
		}

		final Field field = property.getField();
		final Class<?> type = field.getType();
		final Object[] constants = type.getEnumConstants();

		for (Object o : constants) {
			Enum<?> e = (Enum<?>) o;

			Object value = EnumUtils.getValue(e);

			if (string.equalsIgnoreCase(value.toString())) {
				return e;
			}
		}

		return null;
	}

	public enum OfString implements Converter<Enum<?>> {

		INSTANCE;

		@Override
		public Class<?> supportJavaTypeKey() {
			return Enum.class;
		}

		@Override
		public CellDataTypeEnum supportExcelTypeKey() {
			return CellDataTypeEnum.STRING;
		}

		@Override
		public Enum<?> convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			String string = cellData.getStringValue();
			return of(string, contentProperty);
		}

		@Override
		public WriteCellData<?> convertToExcelData(Enum<?> value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			return toCellData(value);
		}

	}

	public enum OfNumber implements Converter<Enum<?>> {

		INSTANCE;

		@Override
		public Class<?> supportJavaTypeKey() {
			return Enum.class;
		}

		@Override
		public CellDataTypeEnum supportExcelTypeKey() {
			return CellDataTypeEnum.NUMBER;
		}

		@Override
		public Enum<?> convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			BigDecimal decimal = cellData.getNumberValue();
			return of(decimal, contentProperty);
		}

		@Override
		public WriteCellData<?> convertToExcelData(Enum<?> value, ExcelContentProperty contentProperty,
				GlobalConfiguration globalConfiguration) throws Exception {
			return toCellData(value);
		}

	}

}
