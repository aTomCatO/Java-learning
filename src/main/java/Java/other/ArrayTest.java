package Java.other;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * @author XYC
 */
public class ArrayTest {
    private static final int[] array = new int[21];

    static {
        Random r = new Random();
        for (int i = 0; i < array.length; i++) {
            //为数组随机生成元素
            while (true) {
                int num = r.nextInt(21) + 1;
                //判断该随机数是否能使用(num没有重复),默认为true
                boolean flag = true;
                // 当i=0时,不进行此循环
                for (int j = 0; j < i; j++) {
                    // 如果这个num在前面已生成并储存于数组,将不能在使用
                    if (array[j] == num) {
                        flag = false;
                        break;
                    }
                }
                //num没有重复,可以使用
                if (flag) {
                    array[i] = num;
                    break;
                }
            }
        }
        for (int j : array) {
            System.out.print(j + "\t");

        }
        System.out.println();
    }

    /**
     * 数组元素以数组长度的一半为中心作元素位置反转
     */
    @Test
    public void test2() {
        for (int n = 0, m = array.length - 1; n < m; n++, m--) {
            // | (n)⋙ | ⋘(m) |
            int tmp = array[n];
            array[n] = array[m];
            array[m] = tmp;
        }
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
        System.out.println("");
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 1; j++) {
                if (array[i] > array[j]) {
                    int max = array[j];
                    array[j] = array[i];
                    array[i] = max;
                }
            }
        }
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i] + "\t");
        }
        System.out.println("");
    }

    /**
     * 把数组元素以三角形形状输出,方案一:
     */
    @Test
    public void test3() {
        int j = 1;
        int increase = 1;
        for (int i = 0; i < array.length; i++) {
            if (i == j) {
                j += increase;
                increase = 1;
                System.out.println("---" + i + "\t" + j + "---");
            }
            increase += 1;
            System.out.print(array[i] + "\t");
        }
    }

    /**
     * 把数组元素以三角形形状输出,方案二:
     */
    @Test
    public void test4() {
        int j = 0;
        int m = 1;
        int n = 0;
        for (int i = 0; i <= m & j < array.length; i++) {
            n = (i + m) - 1;
            while (j <= n) {
                System.out.print(array[j] + "\t");
                j++;
                m++;
            }
            System.out.println("");
        }
    }

    /**
     * 把最大的数放在数组的第一个位置,最小的数放在数组的最后一个位置
     */
    public void main(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    @Test
    public void test5() {
        int max = 0;
        int min = 99;
        int maxIndex = -1, minIndex = -1;
        for (int i = 0; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
                maxIndex = i;
            }
            if (min > array[i]) {
                min = array[i];
                minIndex = i;
            }
        }
        //让索引为0的元素与最大的元素交换位置
        main(array, 0, maxIndex);
        //让最后一个索引的元素与最小的元素交换位置
        main(array, array.length - 1, minIndex);
        System.out.println(Arrays.toString(array));
    }

    @Test
    public void text6() {
        //生成不重复的字符串
        String[] strings = {"1", "2", "3", "4"};
        for (int i = 0; i < strings.length; i++) {
            for (int j = 0; j < strings.length; j++) {
                if (j == i) {
                    continue;
                }
                for (int k = 0; k < strings.length; k++) {
                    if (k == i || k == j) {
                        continue;
                    }
                    System.out.print(strings[i] + strings[j] + strings[k] + "\t");
                }
            }
        }
    }

    @Test
    public void text7() {
        //打印三角形
        int n = 4;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i + 1; j++) {
                System.out.print("*");
            }
            System.out.println("");
        }
        int m = 3;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m - i; j++) {
                System.out.print("*");
            }
            System.out.println("");
        }
    }

}
