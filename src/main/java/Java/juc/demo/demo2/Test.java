package Java.juc.demo.demo2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XYC
 */
@Slf4j()
public class Test {
    private static final FishPool fishPool = new FishPool(2);
    private static final CountDownLatch start = new CountDownLatch(1);
    private static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 20;
        end = new CountDownLatch(threadCount);
        int getCount = 1;
        AtomicInteger Got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 1; i <= threadCount; i++) {
            new Thread(new FishRunner(getCount, Got, notGot)).start();
        }
        start.countDown();
        end.await();
        System.out.println("总获取次数: " + (threadCount * getCount));
        System.out.println("获取到连接的次数: " + Got);
        System.out.println("未获取到连接的次数: " + notGot);
    }

    @AllArgsConstructor
    static class FishRunner implements Runnable {
        int count;
        /**分别统计获取到连接的数量Got,和未获取连接的数量notGot*/
        AtomicInteger got, notGot;

        @Override
        public void run() {
            try {
                start.await();
            } catch (Exception ignored) {

            }
            while (count > 0) {
                try {
                    Fish fish = fishPool.getFish();
                    if (fish != null) {
                        try {
                            log.info(fish.getName());
                            TimeUnit.SECONDS.sleep(1);
                        } finally {
                            fishPool.releaseFish(fish);
                            got.incrementAndGet();
                        }
                    } else {
                        notGot.incrementAndGet();
                    }
                } catch (Exception ignored) {
                } finally {
                    count--;
                }
            }
            end.countDown();
        }
    }
}
