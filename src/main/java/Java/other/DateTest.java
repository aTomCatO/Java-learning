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
    /**
     * yyyy年 MM月 dd日
     * hh（12小时制） HH（24小时制）
     * mm分 ss秒 SSS毫米
     */
    @Test
    public void dateFormat() {
        Date date = new Date();
        System.out.println("1  " + DateFormat.getDateInstance().format(date));
        System.out.println("2  " + DateFormat.getDateInstance(DateFormat.FULL).format(date));
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        System.out.println("3  " + dateFormat.format(date));

        System.out.println("4  " + dateFormat.format(new Date(System.currentTimeMillis() + 30 * 60 * 1000)));
    }

    @Test
    public void localDataTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime thisWeek = now
                .withHour(18)
                .withMinute(0)
                .withSecond(0)
                .withNano(0)
                .with(DayOfWeek.THURSDAY);

        LocalDateTime nextWeek = thisWeek.plusWeeks(1);
        System.out.println("当前日期: " + now);
        System.out.println("本周四: " + thisWeek);
        System.out.println("下周四: " + nextWeek);
        System.out.println("当前距离下周四还有 " + Duration.between(now, nextWeek).toDays() + " 天");
    }

    @Test
    public void locale() {
        Locale usLocale = new Locale("en", "US");
        Locale zhLocale = new Locale("zh", "CN");

        Date now = new Date();
        DateFormat usDateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, usLocale);
        DateFormat zhDateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, zhLocale);
        System.out.println("美国：" + usDateFormat.format(now));
        System.out.println("中国：" + zhDateFormat.format(now));
    }
}
