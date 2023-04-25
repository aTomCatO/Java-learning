package strengthen.类的实例化步骤;

/**
 * @author XYC
 */
public class Father {
    private int a = ordinary();
    private static int b = hasStatic();

    {
        System.out.println("Father 普通代码块：" + 3);
    }

    static {
        System.out.println("Father 静态代码块：" + 1);
    }

    public Father() {
        System.out.println("Father 构造器：" + 2);
    }


    public int ordinary() {
        System.out.println("Father 普通方法：" + 4);
        return 4;
    }

    public static int hasStatic() {
        System.out.println("Father 静态方法：" + 5);
        return 5;
    }


}
