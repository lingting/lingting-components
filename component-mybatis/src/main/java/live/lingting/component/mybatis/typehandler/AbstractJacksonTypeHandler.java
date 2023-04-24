package live.lingting.component.mybatis.typehandler;

import live.lingting.component.core.util.StringUtils;
import live.lingting.component.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lingting 2022/9/28 14:43
 */
@Slf4j
public abstract class AbstractJacksonTypeHandler<T> extends BaseTypeHandler<T> {

	protected T parse(String json) {
		try {
			if (StringUtils.hasText(json)) {
				return toObject(json);
			}
		}
		catch (Exception e) {
			log.error("数据库数据转化对象异常! cls: {}; json: {}", getClass().getName(), json);
		}
		return defaultValue();
	}

	protected String resolve(T obj) {
		try {
			if (obj != null) {
				return toJson(obj);
			}
		}
		catch (Exception e) {
			log.error("对象转化json异常! cls: {}; json: {}", getClass().getName(), obj);
		}
		return defaultJson();
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
		ps.setString(i, resolve(parameter));
	}

	@Override
	public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
		return parse(rs.getString(columnName));
	}

	@Override
	public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
		return parse(rs.getString(columnIndex));
	}

	@Override
	public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
		return parse(cs.getString(columnIndex));
	}

	/**
	 * 从数据库取出的数据转化为对象
	 * @param json 数据库存储数据
	 * @return 实体类对象
	 */
	protected abstract T toObject(String json);

	/**
	 * 将实体类对象转化为数据库存储数据
	 * @param t 实体类对象
	 * @return 数据库存储数据
	 */
	protected String toJson(T t) {
		return JacksonUtils.toJson(t);
	}

	/**
	 * 取出数据转化异常时 使用
	 * @return 实体类对象
	 */
	protected abstract T defaultValue();

	/**
	 * 存储数据异常时 使用
	 * @return 存储数据
	 */
	protected abstract String defaultJson();

}
