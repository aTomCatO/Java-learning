package Java.juc.demo.demo1;

import lombok.AllArgsConstructor;

import java.sql.Connection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XYC
 */
public class Test {
    static ConnectionPool connectionPool = new ConnectionPool(10);
    /**
     * 保证所有线程能够同时开始执行
     */
    static CountDownLatch start = new CountDownLatch(1);
    static CountDownLatch end;

    public static void main(String[] args) throws InterruptedException {
        int threadCount = 20;
        end = new CountDownLatch(threadCount);
        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();
        for (int i = 1; i <= threadCount; i++) {
            new Thread(new ConnectionRunner(count, got, notGot)).start();
        }
        start.countDown();
        end.await();
        System.out.println("总获取次数: " + (threadCount * count));
        System.out.println("获取到连接的次数: " + got);
        System.out.println("未获取到连接的次数: " + notGot);
    }

    @AllArgsConstructor
    static class ConnectionRunner implements Runnable {
        int count;
        AtomicInteger Got, notGot;//分别统计获取到连接的数量Got,和未获取连接的数量notGot

        @Override
        public void run() {
            try {
                start.await();
            } catch (Exception ignored) {

            }
            while (count > 0) {
                try {
                    Connection connection = connectionPool.fetchConnection(1000L);
                    if (connection != null) {
                        try {
                            connection.createStatement();
                            connection.commit();
                        } finally {
                            connectionPool.releaseConnection(connection);
                            Got.incrementAndGet();
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
