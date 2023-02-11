package Java.proxy.simulate.cglibsimulate;

import Java.proxy.javabean.Hero;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;

/**
 * @author XYC
 * Cglib 动态代理模拟 —— 代理类模拟
 *                     一个代理类对应俩个 FastClass类
 */
@NoArgsConstructor
@AllArgsConstructor
public class CglibProxy extends Hero {
    private MethodInterceptor methodInterceptor;
    private static final Method proxySetAbility;
    private static final Method proxyUseAbility;
    private static final MethodProxy originSetAbility;
    private static final MethodProxy originUseAbility;

    static {
        try {
            proxySetAbility = Hero.class.getMethod("setAbility", String.class);
            proxyUseAbility = Hero.class.getMethod("useAbility");

            /**
             * Class c1：被代理类的 Class 对象
             * Class c2：代理类的 Class 对象
             * String desc：方法的描述信息
             *              ()V                                     代表无参数且返回值类型是 void
             *              (I)I                                    代表一个int类型的参数且返回值类型是 int
             *              (J)Z                                    代表一个long类型的参数且返回值类型是 boolean
             *              (Ljava/lang/String;)Ljava/lang/String;  代表一个String类型的参数及返回值类型是 String
             *
             * String name1：带增强功能的方法
             * String name2：带原始功能的方法
             * */
            originSetAbility =
                    MethodProxy.create
                            (Hero.class, CglibProxy.class, "(Ljava/lang/String;)V", "setAbility", "originSetAbility");
            originUseAbility =
                    MethodProxy.create
                            (Hero.class, CglibProxy.class, "()V", "useAbility", "originUseAbility");

            /**
             * MethodProxy 中的 init 方法：
             * 通过CreateInfo和Signature创建FastClassInfo对象。
             * 在创建FastClassInfo的时候，会动态的生成两个类。
             * 他们会继承FastClass对象，并且赋值为FastClassInfo的f1和f2属性，
             * 然后再生成的类里面通过方法的签名找下标，赋值为FastClassInfo的i1和i2属性。
             * 这个下标对应的生成的FastClass里面getIndex方法返回的值。
             * 也就是FastClassInfo的f1和f2的属性为代理类和被代理类生成的FastClass对象，
             * i1和i2保存的是这次调用方法再各自的FastClass对象里面的下标，这个下标是生成的类的getIndex方法返回的下标。
             * 在这个 init 方法中调用 helper 方法，
             * helper 方法里面会通过Cglib来生成类，它肯定有一个内部类，
             * 然后继承于AbstractClassGenerator，实现generateClass方法，然后自己通过ClassEmitter的子类来编写这个类。
             * */
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void originSetAbility(String ability) {
        super.setAbility(ability);
    }

    public void originUseAbility() {
        super.useAbility();
    }

    @Override
    public void setAbility(String ability) {
        try {
            this.ability = ability;
            methodInterceptor.intercept(this, proxySetAbility, new Object[]{ability}, originSetAbility);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void useAbility() {
        try {
            methodInterceptor.intercept(this, proxyUseAbility, new Object[]{}, originUseAbility);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
