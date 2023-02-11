package study;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author XYC
 * 日历输出
 */
public class Calender {
    private static Pattern pattern = Pattern.compile("(\\d{4})\\S(\\d{2})");

    /**
     * 需要实现的目标：根据输入的年月打印出本月的日历表
     * 说明：1900年1月1日刚好是星期一，所以需要计算出从1900 年到当前年月的前一个月总
     * 共经历了几天，然后根据每周七天，用总天数除以7取余数，此余数就是上个月所占到星
     * 期几，也就是每月开头有的空格数，然后打印此空格数，在打印此空格数后再依次打印本
     * 月的各天数。
     **/

    public static void main(String[] args) {
        inputYearAndMonth();
    }

    /**
     * 此方法用于从控制台输入年、月
     **/
    public static void inputYearAndMonth() {
        String yearAndMonth = new SimpleDateFormat("yyyy-MM").format(new Date());
        Matcher matcher = pattern.matcher(yearAndMonth);
        while (matcher.find()) {
            int year = Integer.parseInt(matcher.group(1));
            int month = Integer.parseInt(matcher.group(2));
            printRiLi(year, month);
        }
        /**
         Scanner sc = new Scanner(System.in);
         System.out.println("请输入年");
         int year = sc.nextInt();
         System.out.println("请输入月");
         int month = sc.nextInt();
         */
    }

    /**
     * 打印日历
     **/
    public static void printRiLi(int year, int month) {
        //一周七天，定义一个计数器，打印一天加1（包括空白）如果%7等于0的话就需要换行
        int count = 0;
        System.out.println("-----" + year + "年" + month + "月的日历表-----");
        System.out.println();
        System.out.println("一\t二\t三\t四\t五\t六\t日");
        for (int i = 1; i <= getSpace(year, month); i++) {
            System.out.print("\t");
            count += 1;
        }
        for (int i = 1; i <= getDaysOfMonth(year, month); i++) {
            System.out.print(i + "\t");
            count += 1;
            if (count % 7 == 0) {
                System.out.println();
            }
        }

    }

    /**
     * 判断年份是平年还是闰年(用于判断一年有365天或366天，并用于判断2月有28天或29天),
     * 返回值是true(29天、366天)和false(28天、365天)
     **/
    public static boolean isLoopYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    /**
     * 获得某月的天数
     **/
    public static int getDaysOfMonth(int year, int month) {
        int days = 0;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = isLoopYear(year) ? 29 : 28;
                break;
            default:
                break;
        }
        return days;
    }

    /**
     * 获得自1900年至当前年、月经过的总天数
     * 实现：1900年到year - 1 年的总天数
     * 当年至month - 1 的总在数
     * 两个天数之和相加
     **/
    public static int getTotalDays(int year, int month) {
        int daysofyear = 0;
        int daysofmonth = 0;
        for (int i = 1900; i < year; i++) {

            daysofyear += isLoopYear(i) ? 366 : 365;
        }
        for (int i = 1; i < month; i++) {

            daysofmonth += getDaysOfMonth(year, i);
        }
        return daysofyear + daysofmonth;
    }

    /**
     * 利用总天数模7取余，得到所需要打印的空格数
     **/
    public static int getSpace(int year, int month) {
        return getTotalDays(year, month) % 7;
    }
}
