package live.lingting.component.easyexcel.handler;

import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import live.lingting.component.core.util.CollectionUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自动设置列宽
 *
 * @author lingting 2023-03-27 20:05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomCellWriteHandler extends AbstractColumnWidthStyleStrategy {

	public static final CustomCellWriteHandler INSTANCE = new CustomCellWriteHandler();

	private static final Map<Integer, Map<Integer, Integer>> CACHE = new ConcurrentHashMap<>();

	@Override
	protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell,
			Head head, Integer relativeRowIndex, Boolean isHead) {
		boolean needSetWidth = isHead || !CollectionUtils.isEmpty(cellDataList);
		if (!needSetWidth) {
			return;
		}
		Map<Integer, Integer> maxColumnWidthMap = CACHE.computeIfAbsent(writeSheetHolder.getSheetNo(),
				k -> new ConcurrentHashMap<>());

		Integer columnWidth = this.dataLength(cellDataList, cell, isHead);
		if (columnWidth < 0) {
			return;
		}

		if (columnWidth > 255) {
			columnWidth = 255;
		}

		Integer maxColumnWidth = maxColumnWidthMap.get(cell.getColumnIndex());
		if (maxColumnWidth == null || columnWidth > maxColumnWidth) {
			maxColumnWidthMap.put(cell.getColumnIndex(), columnWidth);
			writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
		}
	}

	private Integer dataLength(List<WriteCellData<?>> cellDataList, Cell cell, Boolean isHead) {
		if (Boolean.TRUE.equals(isHead)) {
			return cell.getStringCellValue().getBytes().length;
		}
		WriteCellData<?> cellData = cellDataList.get(0);
		CellDataTypeEnum type = cellData.getType();
		if (type == null) {
			return -1;
		}
		switch (type) {
			case STRING:
				return cellData.getStringValue().getBytes().length;

			case BOOLEAN:
				return cellData.getBooleanValue().toString().getBytes().length;

			case NUMBER:
				return cellData.getNumberValue().toString().getBytes().length;

			default:
				return -1;

		}
	}

}
