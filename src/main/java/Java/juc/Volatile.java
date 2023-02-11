package Java.juc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.locks.LockSupport;

/**
 * @author XYC
 */
@Slf4j()
public class Volatile {
    private static int energy = 0;
    private static boolean isProduce = true;
    private static boolean isPark = false;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    static class Person {
        private String name;
        private Integer age;
    }

    public static void main(String[] args) throws InterruptedException {
        Thread consumer = new Thread(new Runnable() {
            public void run() {
                while (isProduce || energy > 0) {
                    while (energy == 0) {
                        log.info("等待能量的提供...当前能量为: " + energy);
                        isPark = true;
                        LockSupport.park();
                    }
                    log.info("消耗能量中..." + --energy);
                }
            }
        }, "消费者");
        Thread producer = new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i < 5000; i++) {
                    log.info("补充能量中..." + ++energy);
                    while (isPark) {
                        isPark = false;
                        LockSupport.unpark(consumer);
                    }
                }
                log.info("能量补充完毕");
                LockSupport.unpark(consumer);
                isProduce = false;
            }
        }, "生产者");
        long startTime = System.currentTimeMillis();
        System.out.println("开始计时: " + startTime);
        consumer.start();
        producer.start();
        consumer.join();
        System.out.println("共耗时: " + (System.currentTimeMillis() - startTime));
    }
}
