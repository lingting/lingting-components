package live.lingting.component.money.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import live.lingting.component.jackson.JacksonUtils;
import live.lingting.component.money.Money;
import live.lingting.component.mybatis.typehandler.AbstractListTypeHandler;

import java.util.List;

/**
 * @author lingting 2023/1/3 15:28
 */
public class MoneyListTypeHandler extends AbstractListTypeHandler<Money> {

	@Override
	protected List<Money> toObject(String json) {
		return JacksonUtils.toObj(json, new TypeReference<List<Money>>() {
		});
	}

}
