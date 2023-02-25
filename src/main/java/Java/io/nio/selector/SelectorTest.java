package Java.io.nio.selector;

import Java.juc.threadpool.MyThreadFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author XYC
 */
public class SelectorTest {
    private static final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    3,
                    100,
                    6, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(5),
                    new MyThreadFactory("worker"));
    private static final List<SelectorWorker> WORKER_LIST;
    private static final Logger LOGGER;

    static {
        WORKER_LIST = new ArrayList<>();
        LOGGER = LoggerFactory.getLogger(SelectorTest.class);
    }

    public static SelectorWorker getWorker() throws IOException {
        SelectorWorker selectorWorker;
        // 阈值，限定每个 worker 处理 selectableChannel 的个数
        int threshold = 2;
        int i;
        for (i = 0; i < WORKER_LIST.size(); i++) {
            selectorWorker = WORKER_LIST.get(i);
            if (selectorWorker.getSelectCount() <= threshold) {
                return selectorWorker;
            }
        }
        i += 1;
        selectorWorker = new SelectorWorker("worker-" + i, Selector.open());
        WORKER_LIST.add(selectorWorker);
        LOGGER.info("new worker-" + i);
        return selectorWorker;
    }

    @Test
    public void server() throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(8888));
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    try {
                        if (selectionKey.isAcceptable()) {
                            LOGGER.info("================isAcceptable=================");
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                        } else if (selectionKey.isReadable()) {
                            LOGGER.info("=================isReadable==================");
                            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            StringBuilder sb = new StringBuilder();
                            while (socketChannel.read(readBuffer) > 0) {
                                readBuffer.flip();
                                sb.append(StandardCharsets.UTF_8.decode(readBuffer));
                                readBuffer.clear();
                            }
                            LOGGER.info("server received:" + sb);
                            selectionKey.interestOps(SelectionKey.OP_WRITE);
                        } else if (selectionKey.isWritable()) {
                            LOGGER.info("=================isWritable==================");
                            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                            writeBuffer.put("server received.".getBytes(StandardCharsets.UTF_8));
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            // 注意SocketChannel.write()方法的调用是在一个while循环中的。
                            // write()方法无法保证能写多少字节到 SocketChannel。
                            // 所以，我们重复调用write()直到Buffer没有要写的字节为止。
                            while (writeBuffer.hasRemaining()) {
                                socketChannel.write(writeBuffer);
                            }
                            selectionKey.cancel();
                        }
                        // 手动从集合中移除当前的 selectionKey, 防止重复处理事件
                        iterator.remove();
                    } catch (IOException e) {
                        // 当客户端断开连接后，如果该连接还有状态未处理则会抛异常。所以应该要把这个状态 cancel 掉
                        selectionKey.cancel();
                    }
                }
            }
        }
    }

    @Test
    public void serverWithWorker() throws IOException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(8888));
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (selector.select() > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    try {
                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            LOGGER.info("================isAcceptable {}=================", socketChannel.hashCode());
                            SelectorWorker worker = getWorker();
                            worker.register(socketChannel, SelectionKey.OP_READ);
                            if (!worker.getAtWork()) {
                                worker.setAtWork(true);
                                LOGGER.info("execute " + worker.getName());
                                executor.execute(worker);
                            }
                        }
                        // 手动从集合中移除当前的 selectionKey, 防止重复处理事件
                        iterator.remove();
                    } catch (IOException e) {
                        // 当客户端断开连接后，如果该连接还有状态未处理则会抛异常。所以应该要把这个状态 cancel 掉
                        selectionKey.cancel();
                    }
                }
            }
            selector.close();
        }
    }

    @Test
    public void client() throws Exception {
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8888))) {
            socketChannel.configureBlocking(false);
            ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
            writeBuffer.put("hello server,I'm Client.".getBytes(StandardCharsets.UTF_8));
            writeBuffer.flip();
            socketChannel.write(writeBuffer);

            Thread.sleep(10000);

            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            StringBuilder sb = new StringBuilder();
            while (socketChannel.read(readBuffer) > 0) {
                readBuffer.flip();
                // sb.append(StandardCharsets.UTF_8.decode(readBuffer));
                sb.append(Charset.defaultCharset().decode(readBuffer));
                readBuffer.clear();
            }
            LOGGER.info(sb.toString());
        }
    }

    @Test
    public void clients() throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            LOGGER.info(String.valueOf(i));
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        client();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        Thread.sleep(60000);
    }
}
