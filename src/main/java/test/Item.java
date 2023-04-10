package test;

import java.math.BigInteger;

public class Item {

    public String group;
    public String type;
    public BigInteger number;
    public BigInteger weight;

    public Item() {
        // no argument constructor required by Jackson
    }

    public Item(String group, String type, int number, int weight) {
        this.group = group;
        this.type = type;
        this.number = BigInteger.valueOf(number);
        this.weight = BigInteger.valueOf(weight);
    }

}
