package Java.proxy.simulate.cglibsimulate.fastclass;

import Java.proxy.javabean.Hero;
import Java.proxy.simulate.cglibsimulate.CglibProxy;
import org.springframework.cglib.core.Signature;

/**
 * @author XYC
 */
public class ProxyFastClass {
    private final Signature s0 = new Signature("originSetAbility", "(Ljava/lang/String;)V");
    private final Signature s1 = new Signature("originUseAbility", "()V");

    /**
     * 获取代理类方法的编号：
     * originSetAbility(String ability)     0
     * originUseAbility()                   1
     * Signature signature 方法签名信息：方法名字、参数、返回值
     */
    public int getIndex(Signature signature) {
        if (s0.equals(signature)) {
            return 0;
        } else if (s1.equals(signature)) {
            return 1;
        }
        return -1;
    }

    /**
     * 根据方法编号正常调用代理对象的方法
     */
    public Object invoke(int index, Object proxy, Object[] args) {
        CglibProxy cglibProxy = (CglibProxy) proxy;
        switch (index) {
            case 0: {
                cglibProxy.originSetAbility((String) args[0]);
                return null;
            }
            case 1: {
                cglibProxy.originUseAbility();
                return null;
            }
            default: {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        ProxyFastClass fastClass = new ProxyFastClass();
        CglibProxy cglibProxy = new CglibProxy();
        int methodID = fastClass.getIndex(new Signature("originSetAbility", "(Ljava/lang/String;)V"));
        fastClass.invoke(methodID, cglibProxy, new Object[]{"透视眼"});

        methodID = fastClass.getIndex(new Signature("originUseAbility", "()V"));
        fastClass.invoke(methodID, cglibProxy, new Object[]{});
    }

}
