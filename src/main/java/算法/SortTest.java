package 算法;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XYC
 * 排序算法
 * <properties>
 * 冒泡排序与选择排序的比较
 * 1，二者平均时间复杂度都是O(n²)
 * 2，选择排序一般要快于冒泡，因为其交换次数少
 * 3，但如果集合有序度高，冒泡优于选择
 * 4，冒泡属于稳定排序算法，而选择属于不稳定排序
 * <properties>
 * 插入排序与选择排序的比较
 * 1，二者平均时间复杂度都是O(n²)
 * 2，大部分情况下，插入都略优于选择
 * 3，有序集合插入的时间复杂度为O(n)
 * 4，插入于稳定排算法，而选择属于不稳定排序
 */
@Slf4j
public class SortTest {
    private final int[] array =
            new int[]{
                    51, 65, 484, 9, 5, 16, 45, 1646, 161, 54, 689, 222,
                    4567, 458, 554, 87, 456, 164, 366, 11, 655, 777, 161,
            };

    /**
     * 冒泡排序(稳定)：
     * 1，比较数组中相邻两个元素大小，若 array[i]>array[j+1]，则交换两个元素，
     * 两两都比较一遍称为一轮冒泡，结果是让最大的元素排至最后。
     * 2，重复以上步骤，直到整个数组有序。
     * <properties>
     * 每轮冒泡时，记录最后一次交换索引 作为 下一轮冒泡的比较次数，
     * 如果这个值为零，表示整个数组有序，结束外层循环
     */
    @Test
    public void bubbleSort() {
        int n = array.length - 1;
        while (n != 0) {
            System.out.println(Arrays.toString(array));
            //lastIndex：记录单轮最后一次交换的索引
            int lastIndex = 0;
            for (int j = 0; j < n; j++) {
                if (array[j] > array[j + 1]) {
                    int min = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = min;
                    lastIndex = j;
                }
            }
            n = lastIndex;
        }
    }

    /**
     * 选择排序（不稳定）
     * 1.将数组分为两个子集，排序的和未排序的，每一轮从未排序的子集中选出最小的元素，放入排序子集
     * 2.重复以上步骤，直到整个数组有序
     */
    @Test
    public void selectSort() {
        for (int i = 0; i < array.length; i++) {
            System.out.println(Arrays.toString(array));
            int minSign = array[i];
            int indexSign = i;
            for (int j = i + 1; j < array.length; j++) {
                if (minSign > array[j]) {
                    minSign = array[j];
                    indexSign = j;
                }
            }
            array[indexSign] = array[i];
            array[i] = minSign;
        }
    }

    /**
     * 插入排序（稳定）
     * 1，将数组分为两个区域，排序区域和未排序区域，每一轮从未排序区域中取出第一个元素，插入到排序区域(需保证顺序)
     * 2，重复以上步骤，直到整个数组有序
     */
    @Test
    public void insertSort() {
        for (int i = 1; i < array.length; i++) {
            System.out.println(Arrays.toString(array));
            int temp = array[i];
            int index = i;
            while (index > 0 && temp < array[index - 1]) {
                array[index] = array[index - 1];
                index -= 1;
            }
            array[index] = temp;
        }
        System.out.println(Arrays.toString(array));
    }

    /**
     * 希尔排序（不稳定）
     * 是插入排序的一种更高效的改进版本
     * <properties>
     * 步长的选择是希尔排序的重要部分。只要最终步长为 1 任何步长序列都可以工作。
     * 算法最开始以一定的步长进行排序。然后会继续以一定步长进行排序，最终算法以步长为1进行排序。
     * 当步长为1时，算法变为插入排序，这就保证了数据一定会被排序。
     * <properties>
     * 希尔增量之间是等比的，这代表着等比之间是可以出现一定的盲区的
     */
    @Test
    public void shellSort() {
        for (int step = array.length / 2; step > 0; step /= 2) {
            for (int i = 0; i < array.length - step; i++) {
                if (array[i] > array[i + step]) {
                    int temp = array[i];
                    array[i] = array[i + step];
                    array[i + step] = temp;
                }
                System.out.println("增量step：" + step + "  组：(" + i + " , " + (step + i) + ")");
                System.out.println(Arrays.toString(array));
                System.out.println("==================================================================");
            }
        }
    }
    /**
     * 快速排序的特点
     * 1、平均时间复杂度是O(nlog₂n)，最坏时间复杂度O(n²)
     * 2、数据量较大时，优势非常明显
     * 3、属于不稳定排序
     * */
    /**
     * 1.单边循环快排
     * ①选择最右元素作为基准点(pivot)元素
     * ②i指针维护小于基准点元素的边界，也是每次交换的目标索引
     * ③j指针负责找到比基准点小的元素，一旦找到则与i进行交换
     * ④最后基准点与i交换，i即为分区位置
     */
    public int quickSort1(int beginIndex, int endIndex) {
        int pivot = array[endIndex];
        int i = beginIndex;
        System.out.println("=========================================================================================");
        System.out.println("基准点：" + pivot);
        System.out.println("排序前：" + Arrays.toString(array));
        while (i < endIndex && array[i] <= pivot) {
            i += 1;
        }
        if (i < endIndex) {
            for (int j = beginIndex; j < endIndex; j++) {
                if (i < j && pivot > array[j]) {
                    int temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                    System.out.println("排序时：" + Arrays.toString(array) + "   i指针(边界索引)：" + i + "   j指针" + j);
                    i += 1;
                }
            }
            array[endIndex] = array[i];
            array[i] = pivot;
        }
        System.out.println("排序后：" + Arrays.toString(array));
        return i;
    }

    public void quickSort1Test(int beginIndex, int endIndex) {
        if (beginIndex >= endIndex) {
            return;
        }
        int i = quickSort1(beginIndex, endIndex);
        quickSort1Test(beginIndex, i - 1);
        quickSort1Test(i + 1, endIndex);
    }

    /**
     * 2.双边循环快排(并不完全等价于hoare霍尔分区方案)
     * ①选择最左元素作为基准点元素
     * ②left指针负责 从左向右 找比 基准点 大的元素，right指针负责 从右向左 找 比基准点 小的元素，
     * 一旦找到二者交换，直至left，right相交
     * ③最后基准点与相交点交换，相交点即为分区位置
     */
    public int quickSort2(int leftIndex, int rightIndex) {
        //左、右指针
        int left = leftIndex;
        int right = rightIndex;
        int pivot = array[leftIndex];

        while (left < right) {
            System.out.println(Arrays.toString(array) + " 基准点：" + pivot + " 左指针：" + left + " 右指针" + right);
            //从右向左 找 比基准点 小的元素
            //while (right > left) {
            //    if (pivot > array[right]) {
            //        break;
            //    }
            //    right -= 1;
            //}
            while (right > left && pivot < array[right]) {
                right -= 1;
            }
            //从左向右 找比 基准点 大的元素
            //while (left < right && (left += 1) < right) {
            //    if (pivot < array[left]) {
            //        break;
            //    }
            //}
            while (left < right && (left += 1) < right && pivot >= array[left]) {

            }
            //左指针 与 右指针 所在元素进行交换，
            //必须保证 右指针 一定在 左指针初始索引（基准点） 的右边，左指针 一定在 右指针初始索引 的 左边
            //避免 指针 与 初始索引 重合，否则就会执行没有意义的交换
            if (leftIndex < right && left < rightIndex) {
                int temp = array[left];
                array[left] = array[right];
                array[right] = temp;
            }
        }
        //基准点与相交点交换，必须保证 左/右指针（相交点） 一定在 基准点 的右边
        //也就是要保证 相交点 和 基准点 不重合 的情况下才能进行交换，不然没有意义
        if (leftIndex < right) {
            int temp = array[right];
            array[right] = pivot;
            array[leftIndex] = temp;
        }
        System.out.println(Arrays.toString(array) + " 基准点：" + pivot + " 左指针：" + left + " 右指针" + right);
        System.out.println("==============================================================================");
        return left;
    }

    public void quickSort2Test(int leftIndex, int rightIndex) {
        if (leftIndex < rightIndex) {
            int endIndex = quickSort2(leftIndex, rightIndex);
            quickSort2Test(leftIndex, endIndex - 1);
            quickSort2Test(endIndex + 1, rightIndex);
        }
    }

    @Test
    public void quickSortTest() {
        quickSort2Test(0, array.length - 1);
    }

    private final String locationStr = "09420";
    private final Integer[] location = convert(locationStr);

    public Integer[] convert(String str) {
        int length = locationStr.length();
        Integer[] integers = new Integer[length];
        for (int i = 0; i < length; i++) {
            char c = locationStr.charAt(i);
            integers[i] = Character.getNumericValue(c);
        }
        return integers;
    }
    public Map<Integer, Integer> location(int start, int end) {
        Map<Integer, Integer> map = new HashMap<>(10);
        Integer elementStart = location[start];
        Integer elementEnd = location[end];
        Integer elementEndBefore = location[end - 1];

        // 如果 elementEndBefore 为 0 ，就不能作为 x 坐标的尾数
        if (elementEndBefore == 0) {
            // 如果 elementEnd 为 0 ，就不能作为 y 坐标的尾数
            if (elementEnd == 0) {
                return location(start, end - 2);
            }
            return location(start, end - 1);
        }

        String x = elementStart + "." + locationStr.substring(start + 1, end - 1);
        if (end == location.length - 1) {
            map.put(Integer.parseInt(x), elementEnd);
        } else {
            String y = elementEnd + "." + locationStr.substring(elementEnd);
            map.put(Integer.parseInt(x), Integer.parseInt(y));
        }
        if (start == end) {
            return map;
        }
        if (elementStart != 0) {
            start += 1;
        }

        if (elementEnd != 0) {
            end -= 1;
        }
        return location(start, end);
    }
}
