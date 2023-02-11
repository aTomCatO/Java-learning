package Java.juc.demo.demo2;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author XYC
 */
@Slf4j()
public class FishPool {
    private final int poolSize;
    private final Fish[] fiches;
    private final AtomicIntegerArray poolArray;
    //private LinkedList<Thread> threadsPark = new LinkedList<>();
    //Random randomThread = new Random();
    //Lock lock = new ReentrantLock();
    //Condition condition = lock.newCondition();
    //boolean isLock = false;
    //private LongAdder remainNum = new LongAdder();
    private final Semaphore semaphore;

    public FishPool(int poolSize) {
        this.poolSize = poolSize;
        this.fiches = new Fish[this.poolSize];
        this.poolArray = new AtomicIntegerArray(new int[this.poolSize]);
        semaphore = new Semaphore(this.poolSize);
        //remainNum.add(this.poolSize);
        for (int i = 0; i < poolSize; i++) {
            fiches[i] = new Fish(i + "号鲤鱼");
        }
        log.info("鱼池初始化已完成");
    }

    /**
     * 乐观锁:
     * 乐观锁一般使用CAS算法或版本号机制实现
     * 总是假设最好的情况,每次去拿数据的时候都认为别人不会修改,所以不会上锁,省去了加锁的开销
     * 也就是没有线程被阻塞的情况下实现变量的同步,所以也叫非阻塞同步（Non-blocking Synchronization）
     * 适用于多读而不适合用于多写(写比较少的情况),如果用于多写的环境,一般会经常产生冲突
     * [===注意===]:
     * 1),自旋CAS(不成功就一直循环执行直到成功),如果长时间不成功,会给CPU带来非常大的执行开销
     * 2),CAS只能保证一个共享变量的原子操作，当操作涉及跨多个共享变量时 CAS 无效。
     * 但是从 JDK 1.5开始，提供了AtomicReference类来保证引用对象之间的原子性，
     * 可以把多个变量放在一个对象里来进行 CAS 操作.
     * 所以我们可以使用锁或者利用AtomicReference类把多个共享变量合并成一个共享变量来操作。
     * 总结:
     * 对于资源竞争较少（线程冲突较轻）的情况，
     * --使用synchronized同步锁进行线程阻塞和唤醒切换以及用户态内核态间的切换操作额外浪费消耗cpu资源；
     * 而CAS基于硬件实现，不需要进入内核，不需要切换线程，操作自旋几率较少，因此可以获得更高的性能。
     * 对于资源竞争严重（线程冲突严重）的情况，
     * --CAS自旋的概率会比较大，从而浪费更多的CPU资源，效率低于synchronized。
     * 补充:
     * Java并发编程这个领域中synchronized关键字一直都是元老级的角色，很久之前很多人都会称它为 “重量级锁” 。
     * 但是，在JavaSE 1.6之后进行了主要包括为了减少获得锁和释放锁带来的性能消耗而引入的 偏向锁 和 轻量级锁
     * 以及其它各种优化之后变得在某些情况下并不是那么重了。
     * synchronized的底层实现主要依靠 Lock-Free 的队列，基本思路是 自旋后阻塞，竞争切换后继续竞争锁，稍微牺牲了公平性，但获得了高吞吐量。
     * 在线程冲突较少的情况下，可以获得和CAS类似的性能；而线程冲突严重的情况下，性能远高于CAS。
     * https://blog.csdn.net/qq_34337272/article/details/81072874
     */
    public Fish getFish() throws InterruptedException {
        semaphore.acquire();
        for (int i = 0; i < fiches.length; i++) {
            if (poolArray.get(i) == 0) {
                if (poolArray.compareAndSet(i, 0, 1)) {
                    log.info("获取到鲤鱼 ==> " + i);
                    return fiches[i];
                }
            }
        }
        return null;
        /**
         while (true) {
         if (remainNum.intValue() > 0) {
         for (int i = 0; i < fiches.length; i++) {
         if (poolArray.get(i) == 0) {
         if (poolArray.compareAndSet(i, 0, 1)) {
         log.info("获取到鲤鱼 ==> " + i);
         remainNum.decrement();
         return fiches[i];
         }
         }
         }
         }
         */
//            threadsPark.addLast(Thread.currentThread());
//            log.info("当前没有可用鲤鱼,进入等待...");
//            LockSupport.park();
    }


    public void releaseFish(Fish releaseFish) {
        int index = 0;
        for (Fish fish : fiches) {
            if (fish == releaseFish) {
                //remainNum.increment();
                poolArray.set(index, 0);
                log.info("释放" + fish.getName());
                semaphore.release();
//                if (!threadsPark.isEmpty()) {
//                    for (int i = 0; i < remainNum.sum(); i++) {
//                        LockSupport.unpark(threadsPark.removeFirst());
////                      LockSupport.unpark(threadsPark.remove(randomThread.nextInt(threadsPark.size())));
//                    }
//                }
            }
            index += 1;
        }
    }
}

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
class Fish {
    private String name;
}