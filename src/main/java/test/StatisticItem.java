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
     * ���������� �������� ������� ����������� � uniqueValues (����� count=1),
     * ����� � notUniqueValues.
     * � ���������� ����� �������������� ������ ����� �� ������ Guava.
     * ������� �� �� ������������, ����� �� ��������� ������ ������������ � ������.
     * ��. https://www.baeldung.com/guava-bloom-filter
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
