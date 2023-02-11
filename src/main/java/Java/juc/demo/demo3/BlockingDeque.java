package Java.juc.demo.demo3;

import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.ArrayDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Queue(队列)是一种常用的数据结构，可以将队列看做是一种特殊的线性表，该结构遵循的先进先出原则。
 * Java中，LinkedList实现了Queue接口,因为LinkedList进行插入、删除操作效率较高
 * 相关常用方法：
 * boolean offer(E e):将元素追加到队列末尾,若添加成功则返回true。
 * E poll():从队首删除并返回该元素。
 * E peek():返回队首元素，但是不删除
 * =======================================================================================
 * Deque(双向队列),是Queue的一个子接口，
 * 双向队列是指该队列两端的元素既能入队(offer)也能出队(poll),
 * 如果将Deque限制为只能从一端入队和出队，则可实现栈的数据结构。
 * 对于栈而言，有入栈(push)和出栈(pop)，遵循先进后出原则
 * 常用方法如下：
 * void push(E e):将给定元素”压入”栈中。存入的元素会在栈首。即:栈的第一个元素
 * E pop():将栈首元素删除并返回。
 *
 * @author XYC
 */
@Slf4j()
public class BlockingDeque<T> {
    private final Deque<T> deque = new ArrayDeque<>();
    private final int capacity;
    private final Lock lock = new ReentrantLock();
    private final Condition produce = lock.newCondition();
    private final Condition consume = lock.newCondition();

    public BlockingDeque(int capacity) {
        this.capacity = capacity;
    }

    public T get() {
        lock.lock();
        try {
            while (deque.isEmpty()) {
                try {
                    log.info("等待获得任务...");
                    consume.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T task = deque.pollFirst();
            produce.signal();
            log.info("获得任务: {}", task);
            return task;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 带超时阻塞获取
     */
    public T get(long timeout) {
        lock.lock();
        try {
            // 将 timeout 统一转换为 纳秒
            long timeOut = TimeUnit.MILLISECONDS.toNanos(timeout);
            while (deque.isEmpty()) {
                try {
                    if (timeOut <= 0) {
                        return null;
                    }
                    log.info("等待获得任务...");
                    // 返回值是剩余时间
                    timeOut = consume.awaitNanos(timeOut);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T task = deque.pollFirst();
            produce.signal();
            log.info("获得任务: {}", task);
            return task;
        } finally {
            lock.unlock();
        }
    }


    public void put(T task) {
        lock.lock();
        try {
            while (deque.size() == capacity) {
                try {
                    log.info("等待加入任务队列: {}", task);
                    produce.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            deque.offerLast(task);
            log.info("加入任务队列: {}", task);
            consume.signal();
        } finally {
            lock.unlock();
        }
    }

    public boolean put(T task, long timeout) {
        lock.lock();
        try {
            long timeOut = TimeUnit.MILLISECONDS.toNanos(timeout);
            while (deque.size() == capacity) {
                try {
                    if (timeOut <= 0) {
                        log.info("超时放弃加入任务队列: {}", task);
                        return false;
                    }
                    log.info("等待加入任务队列: {}", task);
                    timeOut = produce.awaitNanos(timeOut);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            deque.offerLast(task);
            log.info("加入任务队列: {}", task);
            consume.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 策略加入任务队列
     */
    public void tacticsPut(RejectPolicy<T> rejectPolicy, T task) {
        lock.lock();
        try {
            if (deque.size() == capacity) {
                //当任务队列为满时,执行拒绝策略
                rejectPolicy.reject(this, task);
            } else {
                deque.offerLast(task);
                log.info("加入任务队列: {}", task);
                consume.signal();
            }
        } finally {
            lock.unlock();
        }

    }

    public int size() {
        lock.lock();
        try {
            return deque.size();
        } finally {
            lock.unlock();
        }

    }
}
