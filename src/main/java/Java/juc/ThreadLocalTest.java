package Java.juc;

import Java.juc.threadpool.MyThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.springframework.boot.web.servlet.server.Session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author XYC
 */
@Slf4j
public class ThreadLocalTest {
    private static final ThreadLocal<Integer> THREAD_LOCAL = new ThreadLocal<>();
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR =
            new ThreadPoolExecutor(
                    2,
                    Runtime.getRuntime().availableProcessors(),
                    1,
                    TimeUnit.MINUTES,
                    new LinkedBlockingDeque<>(5),
                    new MyThreadFactory("BOSS"),
                    new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {
        try {
            THREAD_POOL_EXECUTOR.execute(new Runnable() {
                @Override
                public void run() {
                    THREAD_LOCAL.set(1);
                    log.info(String.valueOf(THREAD_LOCAL.get()));
                }
            });
            THREAD_POOL_EXECUTOR.execute(() -> {
                THREAD_LOCAL.set(2);
                log.info(String.valueOf(THREAD_LOCAL.get()));
                THREAD_LOCAL.remove();
                log.info(String.valueOf(THREAD_LOCAL.get()));
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            THREAD_POOL_EXECUTOR.shutdown();
        }
    }
}
