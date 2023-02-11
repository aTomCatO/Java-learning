package 面试题.类的实例化步骤;

/**
 * @author XYC
 * 一、类初始化过程：
 * ①一个类要创建实例需要先加载并初始化该类
 * 1、main方法所在的类需要先加载和初始化。
 * ②一个子类要初始化需要先初始化父类。
 * ③一个类初始化就是执行<clinit>()方法。
 * 1，<clinit>()方法由静态类变量显示赋值代码和静态代码块组成。
 * 2，类变量显示赋值代码和静态代码块代码从上到下顺序执行。
 * 3，<clinit>()方法只执行一次。
 * <properties>
 * 二、实例初始化过程：
 * ①实例初始化就是执行<init>()方法：
 * 1，<init>()方法可能重载有多个，有几个构造器就有几个<init>方法。
 * 2，<init>()方法由非静态实例变量显示赋值代码和非静态代码块、对应构造器代码组成。
 * 3，非静态实例变量显示赋值代码和非静态代码块代码从上到下顺序执行，而对应构造器的代码最后执行。
 * 4，每次创建实例对象，调用对应构造器，执行的就是对应的<init>方法。
 * 5，<init>方法的首行是super()或super(实参列表)，即对应父类的<init>方法。
 *
 * 三、类的实例化顺序：
 * 1、父类静态成员
 * 2、子类静态成员
 * 3、父类非静态成员（父类实例成员变量、代码块）
 * 4、父类构造函数
 * 5、子类非静态成员（子类实例成员变量、代码块）
 * 6、子类构造函数
 */
public class Son extends Father {

    private static int b = hasStatic();

    {
        System.out.println("Son 普通代码块：" + 3);
    }

    private int a = ordinary();

    static {
        System.out.println("Son 静态代码块：" + 1);
    }

    public Son() {
        System.out.println("Son 构造器：" + 2);
    }


    @Override
    public int ordinary() {
        System.out.println("Son 普通方法：" + 4);
        return 3;
    }

    public static int hasStatic() {
        System.out.println("Son 静态方法：" + 5);
        return 4;
    }

    public static void main(String[] args) {
        new Son();
        System.out.println();
        new Son();
    }
}
