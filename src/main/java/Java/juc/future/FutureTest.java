package Java.juc.future;

import Java.juc.threadpool.MyThreadFactory;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 异步回调(异步执行,成功回调,失败回调)
 *
 * @author XYC
 */
public class FutureTest {
    private final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            2,
            Runtime.getRuntime().availableProcessors(),
            6,
            TimeUnit.SECONDS,
            new LinkedBlockingDeque<>(6),
            new MyThreadFactory("BOSS"),
            new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * 没有返回值的异步回调
     */
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        Future<Void> completableFuture =
                CompletableFuture.runAsync(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("异步执行");
                    }
                });
        for (int i = 0; i < 100; i++) {
            System.out.println("main执行中...");
        }
        System.out.println("main执行完毕...");
    }

    /**
     * 有返回值的异步回调
     */
    @Test
    public void test2() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture =
                CompletableFuture.supplyAsync(new Supplier<String>() {
                    //供给型接口
                    @Override
                    public String get() {
                        System.out.println("异步执行");
                        return "异步执行完成";
                    }
                });
        for (int i = 0; i < 100; i++) {
            System.out.println("main执行中...");
        }
        System.out.println(
                completableFuture.whenComplete(new BiConsumer<String, Throwable>() {
                    //消费型接口
                    @Override
                    public void accept(String s, Throwable throwable) {
                        //成功回调
                        System.out.println("s => " + s);
                        System.out.println("throwable => " + throwable);
                    }
                }).exceptionally(new Function<Throwable, String>() {
                    //失败回调
                    //函数型接口
                    @Override
                    public String apply(Throwable throwable) {
                        return throwable.getMessage();
                    }
                }).get());
        System.out.println("main执行完毕..");
    }
}
