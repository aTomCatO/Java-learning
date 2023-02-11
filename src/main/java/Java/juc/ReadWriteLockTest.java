package Java.juc;

import org.junit.Test;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.*;

/**
 * @author XYC
 *
 * 独占锁：指该锁一次只能被一个线程所持有。对ReentrantLock和Synchronized而言都是独占锁。
 * 共享锁：指该锁可被多个线程所持有。
 * 多个线程同时读一个资源类没有任何问题，所以为了满足并发量，读取共享资源应该可以同时进行。
 * 但是，如果有一个线程想去写共享资源来，就不应该再有其它线程可以对该资源进行读或写。
 * 对ReentrantReadWriteLock其读锁是共享锁，其写锁是独占锁。
 * 读锁的共享锁可保证并发读是非常高效的，读写，写读，写写的过程是互斥的。
 * 写入的时候不允许其他线程插队(独占锁),而读取的时候允许其他线程插队(共享锁)
 */
public class ReadWriteLockTest {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock  lock = new ReentrantLock();
    CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
    public void read(){
        lock.lock();
        try {
            //lock.readLock().lock();
            System.out.println(Thread.currentThread().getName() + "正在阅读...");
            System.out.println(list);
            System.out.println(Thread.currentThread().getName() + "完成阅读...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            //lock.readLock().unlock();

        }
    }

    public void write(String name) {
        lock.lock();
        //lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "正在写入...");
            list.add(name);
            System.out.println(Thread.currentThread().getName() + "完成写入...");
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            //lock.writeLock().unlock();
        }
    }
    @Test
    public void test() {
        for (int i = 1; i <= 16; i++) {
            new Thread(new Runnable() {
                public void run() {
                    read();
                }
            }, String.valueOf(i)).start();
        }
        for (int i = 1; i <= 16; i++) {
            new Thread(new Runnable() {
                public void run() {
                    write(Thread.currentThread().getName());
                }
            }, String.valueOf(i)).start();
        }
    }
}
