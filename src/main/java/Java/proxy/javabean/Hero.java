package Java.proxy.javabean;

/**
 * @author XYC
 */
public class Hero implements People {
    protected String ability;

    @Override
    public void setAbility(String ability) {
        this.ability = ability;
        System.out.println("拥有 " + ability + " 超能力的英雄");
    }

    public void useAbility() {
        System.out.println("使用 " + ability);
    }
}
