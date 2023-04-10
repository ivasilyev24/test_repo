package test;

import java.util.HashSet;
import java.util.Set;

public class StatisticItem {

    Set<String> uniqValues = new HashSet<>();
    String type;
    int count;

    public StatisticItem(Item item) {
        add(item);
    }

/** Добавление статистики раотает следующим образом:
     * до тех пор, пока нет повторяющегося типа, данные сохранются в uniqValues.
     * Как только обнаружен повтор, Set uniqValues очищается, начинает считаться count.
     * В дальнейшем может использоваться фильтр Блума из пректа Guava.
     * Сеййчас он не используется, чтобы не добавлять лишних зависимостей в проект.
     * см. https://www.baeldung.com/guava-bloom-filter
 */
    public void add(Item item) {
        if (uniqValues != null) {
            if (!uniqValues.contains(item.type)) {
                uniqValues.add(item.type);
            } else {
                uniqValues = null;
                type = item.type;
                count = 2;
            }
        } else if (item.type.equals(type)) {
            count++;
        }
    }

}
