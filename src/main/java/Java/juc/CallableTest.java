package Java.juc;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Runnable没有返回值,Callable有返回值
 * FutureTask —— Runnable 接口的子实现类，
 * FutureTask 对象中封装带有返回值的Callable接口实现类
 *
 * @author XYC
 */

public class CallableTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return "应运而生，前程似锦!";
            }
        });

        new Thread(futureTask).start();

        //获取返回值,这个get方法可能会产生阻塞
        System.out.println(futureTask.get());
    }
}