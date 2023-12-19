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
	void testMaxCount() {
		StepValue simple = StepValue.simple(1, 3L, null);
		assertEquals(0, simple.getCount());
		assertTrue(simple.hasNext());
		assertEquals(1, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(2, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(3, simple.next());
		assertFalse(simple.hasNext());
		assertThrowsExactly(NoSuchElementException.class, simple::next);
	}

	@Test
	void testMaxValue() {
		StepValue simple = StepValue.simple(5, null, 30L);
		assertTrue(simple.hasNext());
		assertEquals(5, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(10, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(15, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(20, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(25, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(30, simple.next());
		assertFalse(simple.hasNext());
		assertThrowsExactly(NoSuchElementException.class, simple::next);
	}

	@Test
	void testCopy() {
		StepValue simple = StepValue.simple(5, null, 50L).start(40).copy();
		assertTrue(simple.hasNext());
		assertEquals(45, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(50, simple.next());
	}

	@Test
	void testStartValue() {
		StepValue simple = StepValue.simple(5, null, 50L).start(40);
		assertTrue(simple.hasNext());
		assertEquals(45, simple.next());
		assertTrue(simple.hasNext());
		assertEquals(50, simple.next());
	}

}
