package Java.juc.forkJoin;

import java.util.concurrent.RecursiveTask;

/**
 * @author XYC
 */
public class ForkJoin extends RecursiveTask<Long> {
    Long start;
    Long end;
    /**
     * 临界值
     */
    Long temp = 10000L;

    public ForkJoin(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        if ((end - start) < temp) {
            long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else {
            //中间值
            long middle = (start + end) / 2;
            ForkJoin task1 = new ForkJoin(start, middle);
            //拆分任务,把任务压入线程队列
            task1.fork();
            ForkJoin task2 = new ForkJoin(middle + 1, end);
            task2.fork();
            return task1.join() + task2.join();
        }
    }
}
