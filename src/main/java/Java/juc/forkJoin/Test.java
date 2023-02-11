package Java.juc.forkJoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

/**
 * @author XYC
 */
public class Test {
    @org.junit.Test
    public void ordinary() {
        Long sum = 0L;
        Long startTime = System.currentTimeMillis();
        for (Long i = 1L; i <= 1000000000; i++) {
            sum += i;
        }
        Long endTime = System.currentTimeMillis();
        System.out.println("num=" + sum + "  耗时:" + (endTime - startTime));
        //num=500000000500000000  耗时:5786
    }

    @org.junit.Test
    public void forkJoin() throws ExecutionException, InterruptedException {
        Long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> task = new ForkJoin(0L, 1000000000L);
        ForkJoinTask<Long> joinTask = forkJoinPool.submit(task);
        Long num = joinTask.get();
        Long endTime = System.currentTimeMillis();
        System.out.println("num=" + num + "  耗时:" + (endTime - startTime));
        //num=500000000500000000  耗时:4479
    }

    @org.junit.Test
    public void excellently() {
        Long startTime = System.currentTimeMillis();
        long sum = LongStream.rangeClosed(0L, 1000000000L).parallel().reduce(0, Long::sum);
        Long endTime = System.currentTimeMillis();
        System.out.println("num=" + sum + "  耗时:" + (endTime - startTime));
        //num=500000000500000000  耗时:125
    }
}
