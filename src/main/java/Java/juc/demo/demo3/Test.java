package Java.juc.demo.demo3;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author XYC
 */
@Slf4j()
public class Test {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2, 6, new RejectPolicy<Runnable>() {
            @Override
            public void reject(BlockingDeque<Runnable> taskDeque, Runnable task) {
                //taskDeque.put(task);                                 // 1) 死等

                taskDeque.put(task, 2000);                     // 2) 带超时等待

                //log.info("放弃任务: {}", task);                       // 3) 让调用者放弃任务执行

                //throw new RuntimeException("执行任务失败: " + task);   // 4) 让调用者抛出异常

                //task.run();                                          // 5) 让调用者自己执行任务
            }
        });
        for (int i = 0; i < 16; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        log.info("执行中...");
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
