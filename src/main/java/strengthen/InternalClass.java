package strengthen;

/**
 * @author XYC
 * 非静态内部类（普通内部类）：
 * 1、在静态成员中，必须依赖外部类的实例对象才能创建非静态内部类的实例对象。
 * 2、可以访问外部类的所有成员。
 * 静态内部类：
 * 1、不必依赖外部类的实例对象就可以创建静态内部类的实例对象。
 * 2、只能访问外部类的静态成员。
 */
public class InternalClass {
    class Internal1 {
        public Internal1() {
            // 外部类的非静态及静态成员都能访问
            test1();
            test2();
        }
    }

    static class Internal2 {
    }

    private void test1() {
        new Internal1();
        new Internal2();
    }

    // public static Internal1 internal1 = new Internal1();

    public static void test2() {
        // 静态成员中不能直接创建 普通内部类 的对象
        // new Internal1()
        new InternalClass().new Internal1();

        // 但是可以直接创建 静态内部类 的对象
        new Internal2();
    }
}
