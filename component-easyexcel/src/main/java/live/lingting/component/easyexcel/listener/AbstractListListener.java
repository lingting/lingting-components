package live.lingting.component.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting 2023-04-12 20:11
 */
public abstract class AbstractListListener<D> extends AnalysisEventListener<D> {

	@Getter
	private final List<D> values = new ArrayList<>();

	@Override
	public void invoke(D data, AnalysisContext analysisContext) {
		Integer rowIndex = analysisContext.readRowHolder().getRowIndex();
		invoke(data, rowIndex, analysisContext);
		getValues().add(data);
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		//
	}

	public abstract void invoke(D data, Integer rowIndex, AnalysisContext analysisContext);

}
