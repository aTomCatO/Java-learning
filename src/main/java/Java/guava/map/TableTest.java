package Java.guava.map;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableTable;
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
 * <p>
 * <p>
 * <p>
 * Guava的Table类实现了Serializable接口，但是使用ObjectMapper的writeValueAsString方法来序列化Table对象是不支持的。
 * 这是因为Table对象的序列化需要保留其底层实现细节，将Table对象作为键值对的形式进行序列化仅会保留Table的基本信息。
 * 如果需要将Table对象序列化为字符串，则可以考虑使用Guava自带的Table实现类ImmutableTable，并将其转换为JSON格式。例如：
 * <p>
 * ImmutableTable<String, String, String> table = ImmutableTable.of(
 * "row1", "col1", "value1",
 * "row1", "col2", "value2",
 * "row2", "col1", "value3",
 * "row2", "col2", "value4"
 * );
 * String json = objectMapper.writeValueAsString(table.rowMap());
 * <p>
 * 通过调用table.rowMap()方法将Table对象转换为行映射Map，然后再将该Map转换为JSON字符串，可以得到包含完整数据的序列化结果。
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
