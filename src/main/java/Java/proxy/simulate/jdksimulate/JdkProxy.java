package Java.proxy.simulate.jdksimulate;

import Java.proxy.javabean.People;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * @author XYC
 * JDK 动态代理模拟 —— 代理类模拟
 */
public class JdkProxy implements People {
    private final InvocationHandler invocationHandler;

    public JdkProxy(InvocationHandler invocationHandler) {
        this.invocationHandler = invocationHandler;
    }

    private static final Method PROXY_METHOD;

    static {
        try {
            PROXY_METHOD = People.class.getMethod("setAbility", String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setAbility(String ability) {
        try {
           invocationHandler.invoke(this, PROXY_METHOD, new Object[]{ability});
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
  