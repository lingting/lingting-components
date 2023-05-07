package live.lingting.component.money;

import live.lingting.component.jackson.JacksonUtils;
import live.lingting.component.money.jackson.MoneyModule;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author lingting 2023-05-07 19:00
 */
class MoneyTest {

	@Test
	void threadLocal() {
		MoneyConfig config = new MoneyConfig(3, RoundingMode.DOWN, 3, "`");
		MoneyScope.set(config);
		Money money = Money.of(new BigDecimal("1584.2348"));
		Assertions.assertEquals(config.quantileSymbol, money.getQuantileSymbol());
		Assertions.assertEquals(config.decimalLimit, money.getDecimalLimit());
		Assertions.assertEquals(config.decimalType, money.getDecimalType());
		Assertions.assertEquals("1584.234", money.toRawString());
		Assertions.assertEquals("1`584.234", money.toPlainString());
		money = Money.of(new BigDecimal("987654321.123456"), 4, RoundingMode.DOWN, 3, ",");
		Assertions.assertEquals("987654321.1234", money.toRawString());
		Assertions.assertEquals("987,654,321.1234", money.toPlainString());
	}

	@Test
	void add() {
		MoneyConfig config = new MoneyConfig(3, RoundingMode.DOWN, 3, "`");
		Money money = Money.of(new BigDecimal("1584.2348"), config);
		Assertions.assertEquals("1584.234", money.toRawString());
		Assertions.assertEquals("1`584.234", money.toPlainString());
		money = money.add(1);
		Assertions.assertEquals("1585.234", money.toRawString());
		Assertions.assertEquals("1`585.234", money.toPlainString());
		money = money.add(1.23);
		Assertions.assertEquals("1586.464", money.toRawString());
		Assertions.assertEquals("1`586.464", money.toPlainString());
		money = money.add(1.2367);
		Assertions.assertEquals("1587.7", money.toRawString());
		Assertions.assertEquals("1`587.7", money.toPlainString());
		money = money.add(new BigDecimal("3.30054"));
		Assertions.assertEquals("1591", money.toRawString());
		Assertions.assertEquals("1`591", money.toPlainString());
		money = money.add(Money.of(new BigDecimal("3.02091"), new MoneyConfig(1, RoundingMode.DOWN, 3, "-")));
		Assertions.assertEquals("1594", money.toRawString());
		Assertions.assertEquals("1`594", money.toPlainString());
	}

	@Test
	void subtract() {
		MoneyConfig config = new MoneyConfig(3, RoundingMode.DOWN, 3, "`");
		Money money = Money.of(new BigDecimal("1584.2348"), config);
		Assertions.assertEquals("1584.234", money.toRawString());
		Assertions.assertEquals("1`584.234", money.toPlainString());
		money = money.subtract(1);
		Assertions.assertEquals("1583.234", money.toRawString());
		Assertions.assertEquals("1`583.234", money.toPlainString());
		money = money.subtract(1.23);
		Assertions.assertEquals("1582.004", money.toRawString());
		Assertions.assertEquals("1`582.004", money.toPlainString());
		money = money.subtract(1.2367);
		Assertions.assertEquals("1580.767", money.toRawString());
		Assertions.assertEquals("1`580.767", money.toPlainString());
		money = money.subtract(new BigDecimal("3.30054"));
		Assertions.assertEquals("1577.466", money.toRawString());
		Assertions.assertEquals("1`577.466", money.toPlainString());
		money = money.subtract(Money.of(new BigDecimal("3.02091"), new MoneyConfig(1, RoundingMode.DOWN, 3, "-")));
		Assertions.assertEquals("1574.466", money.toRawString());
		Assertions.assertEquals("1`574.466", money.toPlainString());
	}

	@Test
	void multiply() {
		MoneyConfig config = new MoneyConfig(3, RoundingMode.DOWN, 3, "`");
		Money money = Money.of(new BigDecimal("1584.2348"), config);
		Assertions.assertEquals("1584.234", money.toRawString());
		Assertions.assertEquals("1`584.234", money.toPlainString());
		money = money.multiply(1);
		Assertions.assertEquals("1584.234", money.toRawString());
		Assertions.assertEquals("1`584.234", money.toPlainString());
		money = money.multiply(1.23);
		Assertions.assertEquals("1948.607", money.toRawString());
		Assertions.assertEquals("1`948.607", money.toPlainString());
		money = money.multiply(1.2367);
		Assertions.assertEquals("2409.842", money.toRawString());
		Assertions.assertEquals("2`409.842", money.toPlainString());
		money = money.multiply(new BigDecimal("3.30054"));
		Assertions.assertEquals("7953.779", money.toRawString());
		Assertions.assertEquals("7`953.779", money.toPlainString());
		money = money.multiply(Money.of(new BigDecimal("3.02091"), new MoneyConfig(1, RoundingMode.DOWN, 3, "-")));
		Assertions.assertEquals("23861.337", money.toRawString());
		Assertions.assertEquals("23`861.337", money.toPlainString());
	}

	@Test
	void divide() {
		MoneyConfig config = new MoneyConfig(3, RoundingMode.DOWN, 3, "`");
		Money money = Money.of(new BigDecimal("1584.2348"), config);
		Assertions.assertEquals("1584.234", money.toRawString());
		Assertions.assertEquals("1`584.234", money.toPlainString());
		money = money.divide(1);
		Assertions.assertEquals("1584.234", money.toRawString());
		Assertions.assertEquals("1`584.234", money.toPlainString());
		money = money.divide(1.23);
		Assertions.assertEquals("1287.995", money.toRawString());
		Assertions.assertEquals("1`287.995", money.toPlainString());
		money = money.divide(1.2367);
		Assertions.assertEquals("1041.477", money.toRawString());
		Assertions.assertEquals("1`041.477", money.toPlainString());
		money = money.divide(new BigDecimal("3.30054"));
		Assertions.assertEquals("315.547", money.toRawString());
		Assertions.assertEquals("315.547", money.toPlainString());
		money = money.divide(Money.of(new BigDecimal("3.02091"), new MoneyConfig(1, RoundingMode.DOWN, 3, "-")));
		Assertions.assertEquals("105.182", money.toRawString());
		Assertions.assertEquals("105.182", money.toPlainString());
	}

	@Test
	void check() {
		MoneyConfig config = new MoneyConfig(3, RoundingMode.DOWN, 3, "`");
		MoneyScope.set(config);
		Money money = Money.of(new BigDecimal("2.510"));
		Assertions.assertFalse(money.isZero());
		Assertions.assertFalse(money.isNegative());
		Assertions.assertFalse(money.isGt(Money.of(new BigDecimal("2.511"))));
		Assertions.assertTrue(money.isGt(Money.of(new BigDecimal("2.50000"))));
		Assertions.assertFalse(money.isGe(Money.of(new BigDecimal("2.511"))));
		Assertions.assertTrue(money.isGe(Money.of(new BigDecimal("2.510"))));

		Assertions.assertFalse(money.isEquals(Money.of(new BigDecimal("2.512"))));
		Assertions.assertTrue(money.isEquals(Money.of(new BigDecimal("2.51"))));

		Assertions.assertFalse(money.isLt(Money.of(new BigDecimal("2.411"))));
		Assertions.assertTrue(money.isLt(Money.of(new BigDecimal("2.512"))));
		Assertions.assertFalse(money.isLe(Money.of(new BigDecimal("2.411"))));
		Assertions.assertTrue(money.isLe(Money.of(new BigDecimal("2.510"))));
	}

	@Test
	void json() {
		JacksonUtils.config(mapper -> mapper.registerModule(new MoneyModule()));

		MoneyConfig config = new MoneyConfig(3, RoundingMode.DOWN, 3, "`");
		MoneyScope.set(config);
		Money money = Money.of(new BigDecimal("5.419"));

		TestDTO dto = new TestDTO();
		dto.setMoney(money);

		String jsonExpected = "{\"money\":\"5.419\"}";

		Assertions.assertEquals(jsonExpected, JacksonUtils.toJson(dto));
		Assertions.assertTrue(money.isEquals(JacksonUtils.toObj(jsonExpected, TestDTO.class).money));
	}

	@Data
	public static class TestDTO {

		private Money money;

	}

}
