package Java.juc.threadpool;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XYC
 */
public class MyThreadFactory implements ThreadFactory {
    private final String factoryName;
    private final AtomicInteger threadID = new AtomicInteger(1);

    public MyThreadFactory(String factoryName) {
        this.factoryName = factoryName;
    }

    @Override
    public Thread newThread(Runnable task) {
        String threadName = factoryName + " - " + threadID.getAndIncrement();
        return new Thread(task, threadName);
    }
}
