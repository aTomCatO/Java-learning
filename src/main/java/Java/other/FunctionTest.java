package Java.other;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

/**
 * 四大函数式接口: lambda表达式,链式编程,函数式接口,Stream流式计算
 * @author XYC
 */

public class FunctionTest {
    @Test
    public void test() {
        User a = new User(1, "a", 16);
        User b = new User(2, "b", 17);
        User c = new User(3, "c", 18);
        User d = new User(4, "d", 19);
        User e = new User(5, "e", 20);
        List<User> users = Arrays.asList(a, b, c, d, e);

        users.stream()
                //断定型接口
                .filter(new Predicate<User>() {
                    @Override
                    public boolean test(User user) {
                        return user.getId() % 2 == 0;
                    }
                })
                //函数型接口
                .map(new java.util.function.Function<User, String>() {
                    @Override
                    public String apply(User user) {
                        return user.getName().toUpperCase(Locale.ROOT);
                    }
                })
                .limit(1)
                .forEach(System.out::println);

        //用lambda表达式简化
        users.stream().filter(user -> {
            return user.getId() % 2 != 0;
        }).map(user -> {
            return user.getName();
        }).sorted((user1, user2) -> {
            return user1.compareTo(user2);
        }).forEach(System.out::println);
    }
}

