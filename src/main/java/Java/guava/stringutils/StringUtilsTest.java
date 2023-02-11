package Java.guava.stringutils;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.junit.Test;

import java.util.function.Consumer;

/**
 * @author XYC
 * 面向对象思想处理字符串
 * 1、Joiner(连接器)：
 * skipNulls()：跳过 null 元素
 * useForNull(String)：对于 null 元素使用其他替代
 * <properties>
 * 2、Splitter(分割器)：
 * trimResults()：
 * omitEmptyStrings()：省略空字符串
 * 3、CharMatcher(字符串匹配器)
 */
public class StringUtilsTest {
    private static final Joiner JOINER = Joiner.on(".").skipNulls();
    private static final Splitter SPLITTER = Splitter.on(".");


    @Test
    public void test1(){
        String join = JOINER.join("a", "b", "c");
        System.out.println(join);
    }
    @Test
    public void test2() {
        String instance = "The doctor said my lung problems relate to my smoking habit.It is an amazing experience to camp in a forest.";

        Iterable<String> split = SPLITTER.split(instance);
        split.forEach(new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        });
    }
}
