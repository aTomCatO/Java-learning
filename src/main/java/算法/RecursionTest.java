package 算法;

/**
 * @author XYC
 * 递归
 */
public class RecursionTest {
    public int step(int num) {
        if (num <= 0) {
            throw new RuntimeException("num 参数不是正整数！");
        }
        if (num == 1 || num == 2) {
            return num;
        }
        return step(num - 2) + step(num - 1);
    }

    public static void main(String[] args) {
        RecursionTest recursionTest = new RecursionTest();
        System.out.println(recursionTest.step(0));
    }
}
