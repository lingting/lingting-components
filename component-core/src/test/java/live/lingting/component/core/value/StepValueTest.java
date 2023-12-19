package live.lingting.component.core.value;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2023-12-19 11:41
 */
class StepValueTest {

	@Test
	void test() {
		StepValue simple3 = StepValue.simple(1, 3L, null);
		assertEquals(0, simple3.getCount());
		assertTrue(simple3.hasNext());
		assertEquals(1, simple3.next());
		assertTrue(simple3.hasNext());
		assertEquals(2, simple3.next());
		assertTrue(simple3.hasNext());
		assertEquals(3, simple3.next());
		assertFalse(simple3.hasNext());
		assertThrowsExactly(NoSuchElementException.class, simple3::next);

		StepValue simple30 = StepValue.simple(5, null, 30L);
		assertTrue(simple30.hasNext());
		assertEquals(5, simple30.next());
		assertTrue(simple30.hasNext());
		assertEquals(10, simple30.next());
		assertTrue(simple30.hasNext());
		assertEquals(15, simple30.next());
		assertTrue(simple30.hasNext());
		assertEquals(20, simple30.next());
		assertTrue(simple30.hasNext());
		assertEquals(25, simple30.next());
		assertTrue(simple30.hasNext());
		assertEquals(30, simple30.next());
		assertFalse(simple30.hasNext());
		assertThrowsExactly(NoSuchElementException.class, simple30::next);
	}

}
