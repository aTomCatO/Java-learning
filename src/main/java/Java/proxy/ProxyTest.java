package Java.proxy;

import Java.proxy.javabean.Hero;
import Java.proxy.javabean.People;
import Java.proxy.simulate.cglibsimulate.CglibProxy;
import Java.proxy.simulate.jdksimulate.JdkProxy;
import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import spring.ApplicationTest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author XYC
 */
public class ProxyTest {
    /**
     * 动态代理 proxy —— jdk动态代理实现
     * 1.Proxy#newProxyInstance() 方法是动态代理的入口，其生成动态代理对象主要有以下几个步骤：
     * (1) getProxyClass0() 方法生成代理类
     * (2) 获取到代理类后将 InvocationHandler 对象入参，反射调用构造方法生成动态代理对象
     * <properties>
     * 2.Proxy#getProxyClass0() 方法其实是从一个 WeakCache 中去获取代理类，
     * 其获取逻辑是如果缓存类中没有代理类的话就调用ProxyClassFactory#apply()，通过代理类工厂去即时生成一个代理类，其步骤如下：
     * (1) 首先通过指定的类加载器去验证目标接口是否可被其加载
     * (2) 通过接口所在包等条件决定代理类所在包及代理类的全限定名称，代理类名称是包名+$Proxy+id
     * (3) 通过 ProxyGenerator.generateProxyClass() 生成字节码数组，
     * 然后调用 native 方法 defineClass0() 将其动态生成的代理类字节码加载到内存中
     * <properties>
     * 3.反射获取到代理类参数为 InvocationHandler.class 的构造器，
     * 其实也就是 Proxy 的带参构造器，调用构造器cons.newInstance(new Object[]{h})生成代理对象
     */
    @Test
    public void test1() {
        //jdk动态代理实现
        People people =
                (People) Proxy.newProxyInstance(
                        ApplicationTest.class.getClassLoader(),
                        new Class[]{People.class},
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                //Object proxy对象实现的接口有： [interface Java.proxy.javabean.People]
                                System.out.println("Object proxy对象实现的接口有： " + Arrays.toString(proxy.getClass().getInterfaces()));
                                System.out.println("Method method 方法名为：" + method.getName());
                                System.out.println("Object[] args 里的元素类型及元素为：");
                                for (Object arg : args) {
                                    System.out.println(arg.getClass().getSimpleName() + " : " + arg);
                                }
                                System.out.println("在方法执行前进行加强...");
                                Object returnValue = method.invoke(new Hero(), args);
                                System.out.println("在方法执行后进行加强...");
                                return returnValue;
                            }
                        });
        people.setAbility("透视眼");
        System.out.println("people 的父类是：" + people.getClass().getSuperclass().getSimpleName());
        System.out.println("people 的接口有：" + Arrays.toString(people.getClass().getInterfaces()));
        //people 的父类是：Proxy
        //people 的接口有：[interface Java.proxy.javabean.People]

    }

    /**
     * 动态代理 proxy —— cglib动态代理实现
     */
    @Test
    public void test2() {
        //enhance v.增强
        Hero hero = (Hero) Enhancer.create(Hero.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("Object proxy对象的父类是：" + proxy.getClass().getSuperclass().getSimpleName());
                System.out.println("Object proxy对象实现的接口有： " + Arrays.toString(proxy.getClass().getInterfaces()));
                //Object proxy对象的父类是：Hero
                //Object proxy对象实现的接口有： [interface org.springframework.cglib.proxy.Factory]
                System.out.println("在方法执行前进行加强...");
                //method.invoke(new Student(), args);
                //MethodProxy 可以避免上面使用反射的方式调用
                //MethodProxy 的 invoke 方法的第一个参数必须是目标对象（被代理的对象）调用的是增强功能
                //methodProxy.invoke(new Hero(), args);
                //MethodProxy 的 invokeSuper 方法的第一个参数必须是 代理的对象 调用的是原始功能
                methodProxy.invokeSuper(proxy, args);
                System.out.println("在方法执行后进行加强...");
                return null;
            }
        });
        hero.setAbility("隐身");

        System.out.println("hero 的父类是：" + hero.getClass().getSuperclass().getSimpleName());
        System.out.println("hero 的接口有：" + Arrays.toString(hero.getClass().getInterfaces()));
        //hero 的父类是：Hero
        //hero 的接口有：[interface org.springframework.cglib.proxy.Factory]
    }

    /**
     * jdk动态代理机制模拟
     */
    @Test
    public void test3() {
        People people = new JdkProxy(new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
                System.out.println("在方法执行前进行加强...");
                Object returnValue = method.invoke(new Hero(), args);
                System.out.println("在方法执行后进行加强...");
                return returnValue;
            }
        });
        people.setAbility("穿透");
    }

    @Test
    public void test4() {
        Hero hero = new Hero();
        CglibProxy proxy = new CglibProxy(new MethodInterceptor() {
            @Override
            public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                System.out.println("在方法执行前进行加强...");
                //method.invoke(hero, args);
                methodProxy.invokeSuper(proxy, args);
                System.out.println("在方法执行后进行加强...");
                System.out.println("===============================================");
                return null;
            }
        });
        proxy.setAbility("守护");
        proxy.useAbility();
    }
}
