package test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.*;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Application1 {

    private static final int KILOBYTE = 1024;

     public static void main(String... params) {
        while (true) {
            try {
                System.out.println("¬ведите им€ csv или json файла или exit дл€ выхода:");
                String name = "";
                Scanner scanner = new Scanner(System.in);  // Create a Scanner object
                name = scanner.nextLine();  // Read user input
                //name = "C:\\Users\\IVasilev\\Projects\\test4\\module1\\src\\main\\resources\\out_litle.json";
                //name = "C:\\Users\\IVasilev\\Projects\\test4\\module1\\src\\main\\resources\\out_litle.csv";
                //name = "C:\\Users\\IVasilev\\Projects\\test4\\module1\\src\\main\\resources\\out.json";
                //name = "C:\\Users\\IVasilev\\Projects\\test4\\module1\\src\\main\\resources\\out.csv";

                if (name.endsWith(".csv")) {
                    readCSV(name);
                    //return;
                } else if (name.endsWith(".json")) {
                    readJson(name);
                    //return;
                } else if (name.equals("exit")) {
                    return;
                } else {
                    System.out.println("Ќе известное расширение файла: " + name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void readCSV(String name) {
        initStatistics();
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.builder().setUseHeader(true).
                addColumn("group").addColumn("type").addColumn("number").addColumn("weight").
                build();
        ObjectReader oReader = csvMapper.reader(Item.class).with(schema);
        try (Reader reader = new FileReader(name)) {
            MappingIterator<Item> mi = oReader.readValues(reader);
            int index = 0;
            while (mi.hasNext()) {
                Item current = mi.next();
                collectStatistics(current);
                index++;
                if (index % 10 == 0) {
                    System.out.println(index);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        printStatistics();
    }

    private static void readJson(String fileName) {
        initStatistics();
        JsonFactory jfactory = new JsonFactory();
        try (JsonParser jParser = jfactory.createParser(new BufferedInputStream(new FileInputStream(fileName),
                KILOBYTE*2))) {
            if (jParser.nextToken() == JsonToken.START_ARRAY) {
                jParser.nextToken();
            } else {
                throw new IllegalArgumentException("documents should be a list");
            }
            int index = 0;
            while (true) {
                if (jParser.getCurrentToken() == JsonToken.END_ARRAY) {
                    break;
                }
                index++;
                readObject(jParser);

                if (index % 10 == 0) {
                    System.out.println(index);
                }
            }
            printStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void readObject(JsonParser jParser) throws IOException {
        if (jParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw new IllegalArgumentException("missing object");
        }
        String group = null, type = null;
        BigInteger number = null, weight = null;
        while (jParser.nextToken() != JsonToken.END_OBJECT) {
            String name = jParser.getCurrentName();
            if ("group".equals(name)) {
                jParser.nextToken();
                group = jParser.getText();
            } else if ("type".equals(name)) {
                jParser.nextToken();
                type = jParser.getText();
            } else if ("number".equals(name)) {
                jParser.nextToken();
                number = jParser.getBigIntegerValue();
            } else if ("weight".equals(name)) {
                jParser.nextToken();
                weight = jParser.getBigIntegerValue();
            }
        }
        if (jParser.getCurrentToken() == JsonToken.END_OBJECT) {
            jParser.nextToken();
        }

        Item current = new Item(group, type, number.intValue(), weight.intValue());
        collectStatistics(current);
    }


    private static void printStatistics() {
        System.out.println();
        System.out.println("/*************/");
        System.out.println("/* —татистика /");
        System.out.println("/*************/");
        System.out.println();
        System.out.println("ƒубликаты объектов (объекты с одинаковой группой(group) и типом (type)) с количеством их повторений.");
        System.out.println("=========================================================================================================");
        for (Map.Entry<String, StatisticItem> e : map1.entrySet()) {
            System.out.printf("group: %s", e.getKey());
             System.out.printf(" ѕары type и count: %s", e.getValue().notUniqueValues.entrySet().toString());
            System.out.println();
        }
        System.out.println();
        System.out.println("—уммарный вес (УweightФ) объектов в каждой группе(УgroupФ)");
        System.out.println("==========================================================");
        for (Map.Entry<String, BigInteger> e : sumWeights.entrySet()) {
            System.out.printf("group: %s weight: %d", e.getKey(), e.getValue());
            System.out.println();
        }
        System.out.println();
        System.out.printf("ћаксимальный и минимальный веса объектов в файле: %d %d\n", max, min);
        System.out.println("=======================================================");;
    }

    private static void collectStatistics(Item current) {
        StatisticItem si = map1.get(current.group);
        if (si == null) {
            si = new StatisticItem(current);
            map1.put(current.group, si);
        }
        si.add(current);
        BigInteger val2 = sumWeights.get(current.group);
        val2 = val2 == null ? current.weight : val2.add(current.weight);
        sumWeights.put(current.group, val2);

        max = max.max(current.weight);
        min = min.min(current.weight);
    }

    // ƒубликаты объектов (объекты с одинаковой группой(УgroupФ) и типом (УtypeФ)) с количеством их повторений.
    private static Map<String, StatisticItem> map1 = new HashMap();

    // —уммарный вес (УweightФ) объектов в каждой группе(УgroupФ)
    private static Map<String, BigInteger> sumWeights = new HashMap<>();

    // ћаксимальный и минимальный веса объектов в файле.
    private static BigInteger max = BigInteger.ZERO, min = BigInteger.valueOf(Long.MAX_VALUE);

    private static void initStatistics() {
        map1 = new HashMap();
        sumWeights = new HashMap<>();
        max = BigInteger.ZERO;
        min = BigInteger.valueOf(Long.MAX_VALUE);
    }


}
