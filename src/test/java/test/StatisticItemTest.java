package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticItemTest {

    StatisticItem item = new StatisticItem(new Item("0","0", 1, 2));

    @Test
    public void test() {
        item.add(new Item("a","b", 1, 2));
        assertEquals(2, item.uniqueValues.size());
        item.add(new Item("a","c", 1, 2));
        assertEquals(3, item.uniqueValues.size());

        item.add(new Item("a","b", 1, 2));
        assertEquals("[b=2]", item.notUniqueValues.entrySet().toString());

        item.add(new Item("a","b", 1, 2));
        assertEquals("[b=3]", item.notUniqueValues.entrySet().toString());
   }

}