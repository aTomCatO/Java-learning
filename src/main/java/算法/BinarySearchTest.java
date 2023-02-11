package 算法;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author XYC
 * 二分查找
 * 选择 有序数组的 中间数 和需要查找的 目标数 进行比较:
 * 1,中间数  = 目标数  -->  返回
 * 2,中间数 != 目标数:
 * ①中间数  > 目标数 --> 则中间数字 向右 的所有数字都 大于目标值，全部排除
 * ②中间数  < 目标数 --> 则中间数字 向左 的所有数字都 小于目标值，全部排除
 * <properties>
 * <properties>
 * 如果 左边界 与 右边界 的和 除2 的结果为奇数，得到的中间数的 两边 元素的数量 相同（均衡）
 * 如果 左边界 与 右边界 的和 除2 的结果为偶数，得到的中间数的 两边 元素的数量 相差为 1（不均衡）
 * <properties>
 * 奇数二分取中间，偶数二分取中间靠左
 * <properties>
 * 中间数的 两边 元素的数量 不一样 是一定 会出现的情况
 * 但是这种情况并不影响我们对 中间数 和 目标数 大小关系的判断
 * 只要中间数大于目标数，就排除右边的
 * 只要中间数小于目标数，就排除左边的
 * <properties>
 * (leftIndex + rightIndex) / 2 注意：
 * 如果 int 类型的 左边界 右边界 相加 超过 int 类型的 取值范围 就会造成 溢出
 * 解决方案①： leftIndex / 2 + rightIndex / 2
 * 解决方案②： (leftIndex + rightIndex) >>> 1
 * (效率更高。位运算符 >>> 属于无符号位移，就是将操作数所有二进制位不考虑正负数向右移动指定位数)
 * <properties>
 * 在拥有128个元素的数组中二分查找一个数，需要比较的次数最多不超过多少次？
 * 1， 2 的 n 次方 = 128  或  128/2 ... 直到1
 * 2，问题转化为log₂128，如果手边有计算器，用 log₁₀128/log₁₀2
 * 结果为整数，则该整数即为最终结果
 * 结果为小数，则舍去小数部分，整数加一为最终结果
 */
@Slf4j
public class BinarySearchTest {

    private final int target;
    private final int[] array;
    private int leftIndex = 0;
    private int rightIndex;

    public BinarySearchTest(int target, int[] array) {
        this.target = target;
        this.array = Arrays.stream(array).sorted().toArray();
        this.rightIndex = this.array.length;
    }

    public Integer query() {
        int middle = (leftIndex + rightIndex) / 2;
        log.info("(leftIndex + rightIndex) / 2 = ({} + {}) / 2 = {}", leftIndex, rightIndex, middle);
        if (leftIndex <= rightIndex) {
            if (target == array[middle]) {
                return middle;
            } else if (target > array[middle]) {
                leftIndex = middle + 1;
            } else if (target < array[middle]) {
                rightIndex = middle - 1;
            }
        } else {
            return null;
        }
        return query();
    }

    public static void main(String[] args) {
        BinarySearchTest queryTest = new BinarySearchTest(14, new int[]{5, 4, 3, 6, 9, 15, 48, 126, 14, 16, 10});
        System.out.println(Arrays.toString(queryTest.array));
        System.out.println(queryTest.query());
    }
}
