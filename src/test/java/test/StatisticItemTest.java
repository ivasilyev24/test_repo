package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StatisticItemTest {

    StatisticItem item = new StatisticItem(new Item("a","b", 1, 2));

    @Test
    public void test() {
        //item.add(new Item("a","b", 1, 2));
        assertEquals(1, item.uniqValues.size());
        item.add(new Item("a","c", 1, 2));
        assertEquals(2, item.uniqValues.size());

        item.add(new Item("a","b", 1, 2));
        assertNull(item.uniqValues);
        assertEquals("b", item.type);
        assertEquals(2, item.count);

        item.add(new Item("a","b", 1, 2));
        assertEquals(3, item.count);

    }

}