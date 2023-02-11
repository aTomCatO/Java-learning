package Java.reflect.simple;

import org.junit.Test;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * 反射机制学习,反射机制跟配置文件的配合使用
 * get...()只能获取public修饰的成员,否则会出错: java.lang.NoSuchFieldException
 * getDeclared...()则可以获取所有成员,无论是否public修饰
 * private修饰的私有成员不能直接访问,需要使用setAccessible(true)
 * 如果是static修饰的成员,在反射中,例如像这样的方法  invoke(obj)  可以传入一个null
 * @author XYC
 */
public class ReflectTest {
    private static String aClassName;
    private static String methodName;

    //加载配置
    static {
        try {
            Properties properties = new Properties();
            InputStream resourceAsStream =
                    ClassLoader
                            .getSystemClassLoader()
                            .getResourceAsStream("reflect.properties");
            properties.load(resourceAsStream);
            aClassName = properties.get("Class").toString();
            methodName = properties.get("method").toString();
            System.out.println("配置文件信息: " + aClassName + "  :  " + methodName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void firstTest() throws Exception {
        Class<?> aClass = Class.forName(aClassName);
        //创建对象,相当于 new 无参构造器
        Object obj = aClass.newInstance();
        //获取方法
        Method method = aClass.getMethod(methodName);
        System.out.print("执行了isAnimal方法: ");
        //调用方法
        method.invoke(obj);
        System.out.println("类信息: " + aClass + "  :  " + obj + "  :  " + method);

        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            String typeName = field.getType().getTypeName();
            System.out.println(field.get(obj)+" 属性类型检测: " + typeName);
            if ("java.lang.String".equals(typeName)) {
                //setter赋值
                field.set(obj, "汤姆");
                System.out.println("执行了setName方法: " + field.get(obj));
            }
        }
        //获取有参构造器,并通过这个构造器创建对象
        Constructor<?> constructor = aClass.getConstructor(String.class);
        Object Tom = constructor.newInstance("汤姆");
        System.out.print("执行了isAnimal方法: ");
        method.invoke(Tom);
    }

    /**
     * 暴力反射
     */
    @Test
    public void secondlyTest() throws Exception {
        Class<?> aClass = Class.forName(aClassName);
        Constructor<?> constructor = aClass.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Object obj = constructor.newInstance("汤姆", 19);
        Method method = aClass.getMethod(methodName);
        System.out.print("执行了isAnimal方法: ");
        method.invoke(obj);

        Field field = aClass.getDeclaredField("age");
        field.setAccessible(true);
        System.out.println(field.get(obj));
        field.set(obj,20);
        System.out.println(field.get(obj));
    }
}