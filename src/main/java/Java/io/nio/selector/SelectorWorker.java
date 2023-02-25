package Java.io.nio.selector;

import lombok.Data;
import lombok.Synchronized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author XYC
 */
@Data
public class SelectorWorker implements Runnable {
    private String name;
    private Selector selector;
    /**
     * 阈值，限定每个 worker 处理 SelectableChannel 的个数
     */
    private Integer selectCount = 0;
    /**
     * 标记是否在运行
     */
    private Boolean atWork = false;

    private static final Logger LOGGER;

    static {
        LOGGER = LoggerFactory.getLogger(SelectorWorker.class);
    }

    public SelectorWorker(String name, Selector selector) {
        this.name = name;
        this.selector = selector;
    }

    public void register(SelectableChannel selectableChannel, int ops) throws IOException {
        selectableChannel.configureBlocking(false);
        selectableChannel.register(selector, ops);
    }

    @Override
    public void run() {
        try {
            while ((selectCount = selector.select(200)) > 0) {
                LOGGER.info("select selectCount = " + selectCount);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        StringBuilder sb = new StringBuilder();
                        while (socketChannel.read(readBuffer) > 0) {
                            readBuffer.flip();
                            sb.append(StandardCharsets.UTF_8.decode(readBuffer));
                            readBuffer.clear();
                        }
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                        LOGGER.info("=================isReadable {}==================", socketChannel.hashCode());
                        LOGGER.info("server received:" + sb);
                    } else if (selectionKey.isWritable()) {
                        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
                        writeBuffer.put("server received.".getBytes(StandardCharsets.UTF_8));
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        // 注意SocketChannel.write()方法的调用是在一个while循环中的。
                        // write()方法无法保证能写多少字节到 SocketChannel。
                        // 所以，我们重复调用write()直到Buffer没有要写的字节为止。
                        while (writeBuffer.hasRemaining()) {
                            socketChannel.write(writeBuffer);
                        }
                        LOGGER.info("=================isWritable {}==================", socketChannel.hashCode());
                        selectionKey.cancel();
                    }
                    iterator.remove();
                    selectCount -= 1;
                    LOGGER.info("after remove selectCount = " + selectCount);
                }
            }
            LOGGER.info("end work");
            atWork = false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
