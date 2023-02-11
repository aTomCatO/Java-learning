package Java.proxy.simulate.cglibsimulate.fastclass;

import Java.proxy.javabean.Hero;
import org.springframework.cglib.core.Signature;

/**
 * @author XYC
 * FastClass 模拟
 */
public class TargetFastClass {
    private final Signature s0 = new Signature("setAbility", "(Ljava/lang/String;)V");
    private final Signature s1 = new Signature("useAbility", "()V");

    /**
     * 获取目标方法的编号：
     * setAbility(String ability)     0
     * useAbility()                   1
     * <properties>
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
     * 根据方法编号正常调用目标对象的方法
     */
    public Object invoke(int index, Object target, Object[] args) {
        Hero hero = (Hero) target;
        switch (index) {
            case 0: {
                hero.setAbility((String) args[0]);
                return null;
            }
            case 1: {
                hero.useAbility();
                return null;
            }
            default: {
                return null;
            }
        }
    }

    public static void main(String[] args) {
        TargetFastClass fastClass = new TargetFastClass();
        Hero hero = new Hero();
        int methodID = fastClass.getIndex(new Signature("setAbility", "(Ljava/lang/String;)V"));
        fastClass.invoke(methodID, hero, new Object[]{"透视眼"});

        methodID = fastClass.getIndex(new Signature("useAbility", "()V"));
        fastClass.invoke(methodID, hero, new Object[]{});
    }
}
