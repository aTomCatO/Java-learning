package Java.juc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * 多线程辅助工具类:
 * CountDownLatch,CyclicBarrier,Semaphore
 *
 * @author XYC
 */
@Slf4j()
public class AssistTest {
    /**
     * 减法计数器,CountDownLatch的缺点是,当累减完成后不能复位,也就是不能达到复用的效果
     */
    @Test
    public void countDownLatch() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i <= 6; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + "号归队!");
                    countDownLatch.countDown();
                }
            }, String.valueOf(i)).start();
        }
        //等计数器归零之后才向下执行
        countDownLatch.await();
        System.out.println("队伍集结完毕!");
    }

    /**
     * 循环屏障
     */
    @Test
    public void cyclicBarrier() throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3, () -> System.out.println("执行完毕"));
        new Thread(() -> {
            try {
                log.info("开始...");
                Thread.sleep(3000);
                cyclicBarrier.await();
                log.info("结束...");
            } catch (Exception e) {
            }
        }, "A").start();
        new Thread(() -> {
            try {
                log.info("开始...");
                cyclicBarrier.await();
                //所有线程将会因这个方法阻塞住,等到这个方法被执行了parties次,所有线程才会往下执行,并且复位(达到复用的效果)
                log.info("结束...");
            } catch (Exception e) {
            }
        }, "B").start();
        new Thread(() -> {
            try {
                log.info("开始...");
                cyclicBarrier.await();
                log.info("结束...");
            } catch (Exception e) {
            }
        }, "C").start();
        Thread.sleep(10000);
    }

    /**
     * 信号量,用于限制访问资源线程的数量(限流)
     */
    @Test
    public void semaphore() throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        CountDownLatch countDownLatch = new CountDownLatch(7);
        for (int i = 1; i <= 7; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        log.info(Thread.currentThread().getName() + "号归队!");
                        Thread.sleep(2000);
                        semaphore.release();
                    } catch (Exception e) {
                    } finally {
                        countDownLatch.countDown();
                    }
                }
            }, String.valueOf(i)).start();
        }
        countDownLatch.await();
        log.info("集合完毕");
    }

    @Test
    public void game() throws InterruptedException {
        ExecutorService heroPool = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        String[] percent = new String[10];
        for (int i = 0; i < 10; i++) {
            int heroIndex = i;
            heroPool.submit(() -> {
                for (int j = 0; j <= 100; j++) {
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    percent[heroIndex] = j + "%";
                    System.out.print("\r" + Arrays.toString(percent));
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("\n加载完毕");
        heroPool.shutdown();
    }
}
