package Java.juc.demo.demo3;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

/**
 * @author XYC
 */
@Slf4j()
public class ThreadPool {
    private final BlockingDeque<Runnable> taskDeque;
    private final HashSet<WorkerThread> threads = new HashSet<>();
    private final int coreSize;
    private final RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, int dequeCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.taskDeque = new BlockingDeque<>(dequeCapacity);
        this.rejectPolicy = rejectPolicy;
        log.info("线程池初始化完成");
    }

    public void execute(Runnable task) {
        // 当任务数没有超过 coreSize 时，直接交给 workerThread 对象执行
        // 如果任务数超过 coreSize 时，加入任务队列暂存
        synchronized (threads) {
            if (threads.size() < coreSize) {
                WorkerThread workerThread = new WorkerThread(task);
                threads.add(workerThread);
                workerThread.start();
                log.info("{}开始工作,任务为: {}", workerThread.getName(), task);
            } else {
                //taskDeque.put(task, 2000);
                taskDeque.tacticsPut(rejectPolicy, task);
                log.info("任务{}: 暂无可用工作线程,加入任务队列", task);
            }
        }
    }

    @AllArgsConstructor
    class WorkerThread extends Thread {
        private Runnable task;

        @Override
        public void run() {
            while (task != null || (task = taskDeque.get(2000)) != null) {
                log.info("正在执行任务: {}", task);
                try {
                    task.run();
                } finally {
                    log.info("{}任务执行完毕", task);
                    task = null;
                }
            }
            synchronized (threads) {
                threads.remove(this);
                log.info("移除线程{}", this);
            }
        }
    }
}

/**拒绝策略*/
@FunctionalInterface
interface RejectPolicy<T> {
    /**
     * 拒绝方法
     * @param taskDeque 任务队列
     * @param task 任务对象
     * */
    void reject(BlockingDeque<T> taskDeque, T task);
}