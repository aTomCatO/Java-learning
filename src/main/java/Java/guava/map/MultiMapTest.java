package Java.guava.map;

import com.google.common.collect.ArrayListMultimap;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Multimap - 多值Map:
 * java中的Map维护的是键值一对一的关系，如果要将一个键映射到多个值上，那么就只能把值的内容设为集合形式，
 * guava中的Multimap提供了将一个键映射到多个值的形式，使用起来无需定义复杂的内层集合，可以像使用普通的Map一样使用它
 * @author XYC
 */
public class MultiMapTest {
    static ArrayListMultimap<String, String> multimap = ArrayListMultimap.create();

    /*也可以创建HashMultimap、TreeMultimap等类型的Multimap。*/
    static {
        multimap.put("IT", "Java");
        multimap.put("IT", "Linux");
        multimap.put("武侠", "神雕侠侣");
        multimap.put("武侠", "天涯明月刀");
        multimap.put("武侠", "倚天屠龙记");
    }

    @Test
    public void elementary() {
        System.out.println(multimap);
        //{武侠=[神雕侠侣, 天涯明月刀, 倚天屠龙记], IT=[Java, Linux]}
        List<String> it = multimap.get("IT");
        System.out.println(it);
        //[Java, Linux]
        it.remove(0);
        it.add("C");
        System.out.println(multimap.get("IT"));
        //[Linux, C]
    }

    @Test
    public void secondary() {
        Map<String, Collection<String>> map = multimap.asMap();
        for (String key : map.keySet()) {
            System.out.println(key + " : " + map.get(key));
            //武侠 : [神雕侠侣, 天涯明月刀, 倚天屠龙记] // IT : [Java, Linux]
        }
        map.get("IT").add("Python");
        System.out.println(multimap);
        //{武侠=[神雕侠侣, 天涯明月刀, 倚天屠龙记], IT=[Java, Linux, Python]}
        System.out.println(multimap.size());
        //6
        System.out.println(multimap.keySet().size());
        //2
    }
}
