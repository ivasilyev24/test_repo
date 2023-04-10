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

/** ���������� ���������� ������� ��������� �������:
     * �� ��� ���, ���� ��� �������������� ����, ������ ���������� � uniqValues.
     * ��� ������ ��������� ������, Set uniqValues ���������, �������� ��������� count.
     * � ���������� ����� �������������� ������ ����� �� ������ Guava.
     * ������� �� �� ������������, ����� �� ��������� ������ ������������ � ������.
     * ��. https://www.baeldung.com/guava-bloom-filter
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
