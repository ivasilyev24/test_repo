package test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatisticItem {

    Set<String> uniqueValues = new HashSet<>();
    Map<String,Integer> notUniqueValues = new HashMap<>();

    public StatisticItem(Item item) {
        add(item);
    }

    /**
     * Уникальные значения сначала добавляются в uniqueValues (когда count=1),
     * затем в notUniqueValues.
     * В дальнейшем может использоваться фильтр Блума из пректа Guava.
     * Сеййчас он не используется, чтобы не добавлять лишних зависимостей в проект.
     * см. https://www.baeldung.com/guava-bloom-filter
     */
    public void add(Item item) {
        if (!uniqueValues.contains(item.type)) {
            uniqueValues.add(item.type);
        } else {
            Integer count = notUniqueValues.get(item.type);
            notUniqueValues.put(item.type, count==null? 2 : count+1);
        }
    }

}
