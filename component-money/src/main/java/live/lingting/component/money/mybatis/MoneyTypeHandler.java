package live.lingting.component.money.mybatis;

import live.lingting.component.money.Money;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * mybatis 全局类型处理器
 *
 * @author lingting
 */
@MappedTypes(Money.class)
public class MoneyTypeHandler extends BaseTypeHandler<Money> {

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Money parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setBigDecimal(i, parameter.getValue());
	}

	@Override
	public Money getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return Money.of(rs.getBigDecimal(columnName));
	}

	@Override
	public Money getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return Money.of(rs.getBigDecimal(columnIndex));
	}

	@Override
	public Money getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return Money.of(cs.getBigDecimal(columnIndex));
	}

}
