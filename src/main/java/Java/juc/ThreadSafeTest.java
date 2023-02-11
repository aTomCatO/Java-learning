package Java.juc;

import Java.juc.threadpool.MyThreadFactory;
import lombok.SneakyThrows;
import org.junit.Test;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * @author XYC
 */
public class ThreadSafeTest {
    /**
     * List,Set和Map在多线程的并发下是不安全的,有ConcurrentModificationException异常,并发修改异常
     *
     * 在List中，假设线程A将通过迭代器next()获取下一元素时，从而将其打印出来。
     * 但之前，其他某线程添加新元素至list，结构发生了改变，modCount自增。
     * 当线程A运行到checkForComodification()，expectedModCount是modCount之前自增的值，判定modCount != expectedModCount为真，
     * 继而抛出ConcurrentModificationException。
     * 解决方法：
     * 1、Vector
     * 2、Collections.synchronizedList()
     * 3、CopyOnWriteArrayList（推荐）
     *
     * HashSet也是非线性安全的。（HashSet内部是包装了一个HashMap的）
     * 解决方法:
     * 1、Collections.synchronizedSet(new HashSet<>())
     * 2、CopyOnWriteArraySet<>()（推荐）
     * CopyOnWriteArraySet包装了一个CopyOnWriteArrayList
     *
     * CopyOnWrite 容器即写时复制的容器。待一个容器添加元素的时候，不直接往当前容器Object[]添加，而是先将当前容器Object[]进行copy，
     * 复制出一个新的容器Object[] newElements，然后新的容器Object[ ] newElements里添加元素，添加完元素之后，
     * 再将原容器的引用指向新的容器setArray (newElements)。
     * 这样做的好处是可以对CopyOnWrite容器进行并发的读，而不需要加锁（区别于Vector和Collections.synchronizedList()），
     * 因为当前容器不会添加任何元素。所以CopyOnWrite容器也是一种读写分离的思想，读和写不同的容器。
     *
     * 集合类不安全之Map
     * 解决方法：
     * HashTable
     * Collections.synchronizedMap(new HashMap<>())
     * ConcurrencyMap<>()（推荐）
     */

    @Test
    public void test1() {
        List<Object> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i <= 50; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(Thread.currentThread().getName());
                    System.out.println(Thread.currentThread().getName() + " : " + list);
                }
            }, String.valueOf(i)).start();
        }
    }

    /**
     * 阻塞队列BlockingQueue
     * 一、添加和移除:
     * 1,add(),remove()    当add或remove超出队列容量范围时会有异常抛出
     * 2,offer(),poll()    有返回值且不会抛出异常,offer()的返回值是boolean类型,而poll()的返回值是移除的元素
     * 3,offer(,,),poll(,) 超时退出,当队列满了之后还继续offer(,,)则等待一段时间还是不行就退出;poll(,)同理
     * 4,put(),take()      当队列满了之后还在put()则会造成阻塞,等待成功添加;而take()则是当队列空了之后还在进行此方法也会阻塞
     *
     * 二、检测队首元素:
     * 1,element()         当队列里没有元素时会有异常抛出
     * 2,peek()            有返回值且当队列里没有元素时不会抛异常(返回null)
     */
    @Test
    public void test2() throws InterruptedException {
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(1);
        System.out.println(blockingQueue.offer("A"));
        System.out.println(blockingQueue.offer("B", 6, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(6, TimeUnit.SECONDS));
        System.out.println(blockingQueue.poll(6, TimeUnit.SECONDS));
    }

    /**
     * 同步队列SynchronousQueue
     * 当队列中有元素时,就必须先移除才能再添加
     */
    @Test
    public void test3() {
        BlockingQueue<String> synchronousQueue = new SynchronousQueue<>();
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    synchronousQueue.offer(String.valueOf(i) + Thread.currentThread().getName(), 3, TimeUnit.SECONDS);
                }
            }
        }, "A").start();
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (true) {
                    System.out.println(Thread.currentThread().getName() + " : " + synchronousQueue.poll(3, TimeUnit.SECONDS));
                }
            }
        }, "B").start();
    }
}
