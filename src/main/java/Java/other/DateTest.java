package Java.other;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;


/**
 * @author XYC
 */
public class DateTest {
    @Test
    public void date() {
        Date date = new Date();
        //2021-12-23
        System.out.println("1  " + DateFormat.getDateInstance().format(date));

        //2021年12月23日 星期四
        System.out.println("2  " + DateFormat.getDateInstance(DateFormat.FULL).format(date));

        //2021-12-23  13:34:09
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        System.out.println("3.1  " + dateFormat.format(date));
        dateFormat = new SimpleDateFormat("HHmmssSSS");
        //134354666
        System.out.println("3.2  " + dateFormat.format(date));

        //2021-12-23  01:34:09
        System.out.println("4  " + new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss").format(date));
        /**
         yyyy表示年
         MM表示月
         dd表示天
         hh表示用12小时制
         HH表示用24小时制
         mm表示分
         ss表示秒
         SSS表示毫米
         */
    }

    @Test
    public void localDataTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisWeek = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        LocalDateTime nextWeek = thisWeek.plusWeeks(1);
        System.out.println("当前日期: " + now);
        System.out.println("本周四: " + thisWeek);
        System.out.println("下周四: " + nextWeek);

        System.out.println("当前距离下周四还有(天): " + Duration.between(now, nextWeek).toDays());
        /**
         * 当前日期: 2022-03-20T19:47:17.721
         * 本周四: 2022-03-17T18:00
         * 下周四: 2022-03-24T18:00
         * 当前距离下周四还有(天): 3
         */
    }

    @Test
    public void localeTest() {
        Locale usLocale = new Locale("en", "US");
        Locale zhLocale = new Locale("zh", "CN");

        Date now = new Date();
        DateFormat usDateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, usLocale);
        DateFormat zhDateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, zhLocale);
        System.out.println("美国：" + usDateFormat.format(now));
        System.out.println("中国：" + zhDateFormat.format(now));
    }
}
