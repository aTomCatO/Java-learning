package Java.guava.map;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.junit.Test;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * @author XYC
 * Table - 双键Map:
 * java中的Map只允许有一个key和一个value存在，但是guava中的Table允许一个value存在两个key。
 * Table中的两个key分别被称为rowKey和columnKey，可以看作行和列。
 */
public class TableTest {
    static Table<String, Integer, String> table = HashBasedTable.create();

    static {
        table.put("IT", 1, "Java");
        table.put("IT", 2, "Linux");
        table.put("武侠", 1, "神雕侠侣");
        table.put("武侠", 2, "天涯明月刀");
        table.put("武侠", 3, "倚天屠龙记");

    }

    @Test
    public void elementary() {
        //获取指定的单个value
        System.out.println(table.get("IT", 1));
        //获取所有的rowKey
        Set<String> rowKeys = table.rowKeySet();
        System.out.println(rowKeys);
        //获取所有的columnKey
        Set<Integer> columnKeys = table.columnKeySet();
        System.out.println(columnKeys);
        //获取所有的value
        Collection<String> values = table.values();
        System.out.println(values);

        for (String key : table.rowKeySet()) {
            System.out.println("=====================");
            //{1=Java, 2=Linux}
            Map<Integer, String> rowsData1 = table.row(key);
            System.out.println(rowsData1);
            System.out.println("=====================");
            //[1=Java, 2=Linux]
            Set<Map.Entry<Integer, String>> rowsData2 = table.row(key).entrySet();
            System.out.println(rowsData2);
            System.out.println("=====================");
            for (Map.Entry<Integer, String> data : rowsData2) {
                //1:Java  2:Linux
                System.out.println(data.getKey() + ":" + data.getValue());
            }
            System.out.println("=====================");
        }
    }

    @Test
    public void secondary() {
        Map<String, Map<Integer, String>> rowMap = table.rowMap();
        Map<Integer, Map<String, String>> columnMap = table.columnMap();
        System.out.println(rowMap);
        System.out.println(columnMap);
    }
}
