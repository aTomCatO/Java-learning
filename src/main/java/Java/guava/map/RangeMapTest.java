package Java.guava.map;

import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;
import org.junit.Test;

/**
 * @author XYC
 * RangeMap - 范围Map:
 * 先看一个例子，假设我们要根据分数对考试成绩进行分类，那么代码中就会出现这样丑陋的if-else：
 * public static String getRank(int score){
 * if (0<score && score<60)
 * return "差";
 * else if (60<=score && score<80)
 * return "良";
 * else if (80<=score && score<=90)
 * return "优";
 * ...
 * }
 * 而guava中的RangeMap描述了一种从区间到特定值的映射关系，让我们能够以更为优雅的方法来书写代码。
 */
public class RangeMapTest {
    static RangeMap<Integer, String> rangeMap = TreeRangeMap.create();
    static {
        //(0,60)
        rangeMap.put(Range.open(0,60),"差");
        //[60,80)
        rangeMap.put(Range.closedOpen(60,80),"良");
        //[80,90]
        rangeMap.put(Range.closed(80,90),"优");
        //(90,100]
        rangeMap.put(Range.openClosed(90,100),"🌺");
    }
    @Test
    public void test(){
        System.out.println(rangeMap.get(59));
        System.out.println(rangeMap.get(60));
        System.out.println(rangeMap.get(90));
        System.out.println(rangeMap.get(91));
    }
}
