package Java.guava.map;


import com.google.common.collect.MutableClassToInstanceMap;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XYC
 * ClassToInstanceMap - 实例Map:
 * 取出对象时省去了复杂的强制类型转换，避免了手动进行类型转换的错误。
 * ClassToInstanceMap接口的定义，它是带有泛型的：
 * public interface ClassToInstanceMap<B> extends Map<Class<? extends B>, B>{...}
 * 这个泛型同样可以起到对类型进行约束的作用，value要符合key所对应的类型
 * 所以，如果你想缓存对象，又不想做复杂的类型校验，那么使用方便的ClassToInstanceMap就可以了。
 */
public class ClassToInstanceMap {
    public static void main(String[] args) {
        MutableClassToInstanceMap<Object> instanceMap = MutableClassToInstanceMap.create();
        Cat cat = new Cat("汤姆");
        Dog dog = new Dog("斯派克");
        instanceMap.putInstance(Cat.class, cat);
        instanceMap.putInstance(Dog.class, dog);

        Cat cat1 = instanceMap.getInstance(Cat.class);
        System.out.println("cat == cat1 : " + (cat == cat1));

        System.out.println("=============================");
        Map<Class, Object> map = new HashMap<>();
        map.put(Cat.class, cat);
        map.put(Dog.class, dog);
        Cat cat2 = (Cat) map.get(Cat.class);
        System.out.println("cat == cat2 : " + (cat == cat2));
    }
}

@Data
@AllArgsConstructor
class Cat {
    private String name;
}

@Data
@AllArgsConstructor
class Dog {
    private String name;
}