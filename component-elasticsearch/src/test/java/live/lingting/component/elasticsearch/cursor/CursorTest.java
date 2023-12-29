package live.lingting.component.elasticsearch.cursor;

import live.lingting.component.core.page.PageLimitResult;
import live.lingting.component.core.page.PageScrollResult;
import live.lingting.component.core.value.StepValue;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author lingting 2023-12-29 13:52
 */
class CursorTest {

	@Test
	void limit() {
		LimitCursor<Integer> cursor = new LimitCursor<>(index -> {
			if (index > 1) {
				return PageLimitResult.of(Collections.emptyList());
			}
			StepValue<Integer> stepValue = new StepValue<>(1, (i, p) -> {
				if (i < 3) {
					return (int) (index + i);
				}
				return null;
			});
			return PageLimitResult.of(stepValue.values());
		});

		assertEquals(0, cursor.getCount());
		assertTrue(cursor.hasNext());
		assertEquals(1, cursor.next());
		assertTrue(cursor.hasNext());
		assertEquals(2, cursor.next());
		assertTrue(cursor.hasNext());
		assertEquals(3, cursor.next());
		assertFalse(cursor.hasNext());
	}

	@Test
	void scroll() {
		ScrollCursor<Integer> cursor = new ScrollCursor<>(scrollId -> {
			if (!Objects.equals("2", scrollId)) {
				return PageScrollResult.empty();
			}

			return PageScrollResult.of(Arrays.asList(3, 4), "3");
		}, "2", Arrays.asList(1, 2));
		assertEquals(0, cursor.getCount());
		assertTrue(cursor.hasNext());
		assertEquals(1, cursor.next());
		assertTrue(cursor.hasNext());
		assertEquals(2, cursor.next());
		assertTrue(cursor.hasNext());
		assertEquals(3, cursor.next());
		assertTrue(cursor.hasNext());
		assertEquals(4, cursor.next());
		assertFalse(cursor.hasNext());
	}

	@Test
	void initEmpty() {
		ScrollCursor<Integer> cursor = new ScrollCursor<>(scrollId -> PageScrollResult.empty(), "1",
				Collections.emptyList());
		assertFalse(cursor.hasNext());
	}

	@Test
	void stream() {
		ScrollCursor<Integer> cursor = new ScrollCursor<>(scrollId -> {
			if (!Objects.equals("2", scrollId)) {
				return PageScrollResult.empty();
			}

			return PageScrollResult.of(Arrays.asList(3, 4), "3");
		}, "2", Arrays.asList(1, 2));
		List<Integer> list = cursor.stream().collect(Collectors.toList());
		assertEquals(4, list.size());
		assertEquals(1, list.get(0));
		assertEquals(4, list.get(3));
		assertFalse(cursor.hasNext());
	}

}
