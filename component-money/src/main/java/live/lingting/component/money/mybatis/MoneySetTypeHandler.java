package live.lingting.component.money.mybatis;

import com.fasterxml.jackson.core.type.TypeReference;
import live.lingting.component.jackson.JacksonUtils;
import live.lingting.component.money.Money;
import live.lingting.component.mybatis.typehandler.AbstractSetTypeHandler;

import java.util.Set;

public class MoneySetTypeHandler extends AbstractSetTypeHandler<Money> {

	@Override
	protected Set<Money> toObject(String json) {
		return JacksonUtils.toObj(json, new TypeReference<Set<Money>>() {
		});
	}

}
