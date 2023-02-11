package Java.juc.threadpool;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import java.util.concurrent.*;

/**
 * @author XYC
 */
@Slf4j()
public class ThreadPoolTest {
    /**
     * 线程池设置最大容量:
     * 1,CPU密集型 : Runtime.getRuntime().availableProcessors()
     * 2,IO密集型
     * <properties>
     * 拒绝策略:
     * 1,AbortPolicy() :超载将会抛异常
     * 2,CallerRunsPolicy() :超载的线程将由main线程执行
     * 3,DiscardPolicy() :超载的线程将不会执行
     * 4,DiscardOldestPolicy() :超载的线程将尝试跟其他线程竞争
     */
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            2,
            Runtime.getRuntime().availableProcessors(),
            6,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(6),
            new MyThreadFactory("BOSS"),
            new ThreadPoolExecutor.CallerRunsPolicy());


    @Test
    public void test1() {
        try {
            for (int i = 1; i < 26; i++) {
                log.info("线程池容量：{}  任务队列容量：{}", threadPool.getPoolSize(), threadPool.getQueue().size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                threadPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        log.info("run...");
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            /**
             * 线程池状态变为 SHUTDOWN
             * 不会接收新任务,但会处理阻塞队列剩余任务
             * 此方法不会阻塞调用线程的执行
             */
            threadPool.shutdown();
        }
    }

    /**
     * 可延迟执行任务的线程池
     * <properties>
     * initialDelay秒后开始执行,往后每period/delay秒执行一次(前提需要先前的任务执行完,才会往下执行,避免叠加)
     * 在scheduleAtFixedRate中,如果任务执行所消耗的时间大于period,则这次执行完后的下一次的执行将不再等待period(直接执行下一次),
     * 但在scheduleWithFixedDelay中,无论上一次任务执行了多久,下一次都会在它结束的delay秒后执行
     * 值得注意的是,在执行任务过程中,如果有异常不会主动抛出,需要自己try/catch或者用有返回值的Callable
     */
    @Test
    public void delayed() {
        //如果corePoolSize为 1 , 将为一条线程串行执行多个任务
        ScheduledExecutorService delayed = Executors.newScheduledThreadPool(2);
//        delayed.schedule(() -> {
//            log.info("任务1");
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }, 2, TimeUnit.SECONDS);

//        delayed.schedule(() -> {
//            log.info("任务2");
//        }, 1, TimeUnit.SECONDS);

        delayed.scheduleAtFixedRate(() -> {
            log.info("任务3");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, 1, TimeUnit.SECONDS);

        delayed.scheduleWithFixedDelay(() -> {
            log.info("任务4");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 2, 5, TimeUnit.SECONDS);

        try {
            log.info("main");
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
