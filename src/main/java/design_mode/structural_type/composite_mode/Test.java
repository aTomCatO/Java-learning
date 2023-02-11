package design_mode.structural_type.composite_mode;

/**
 * @author XYC
 */
public class Test {
    public static void main(String[] args) {
        HeadLine root = new HeadLine("目录");

        HeadLine 创建型模式 = new HeadLine("创建型模式");

        HeadLine 单例模式 = new HeadLine("单例模式");
        单例模式.addBook(new Subheading("懒汉单例"));
        单例模式.addBook(new Subheading("饿汉单例"));

        创建型模式.addBook(单例模式);
        创建型模式.addBook(new Subheading("工厂模式"));
        创建型模式.addBook(new Subheading("建造模式"));

        HeadLine 结构型模式 = new HeadLine("结构型模式");
        结构型模式.addBook(new Subheading("合成模式"));
        结构型模式.addBook(new Subheading("代理模式"));
        结构型模式.addBook(new Subheading("桥梁模式"));

        HeadLine 行为型模式 = new HeadLine("行为型模式");
        行为型模式.addBook(new Subheading("观察者模式"));
        行为型模式.addBook(new Subheading("状态模式"));

        root.addBook(创建型模式);
        root.addBook(结构型模式);
        root.addBook(行为型模式);

        System.out.println(root);
    }
}
