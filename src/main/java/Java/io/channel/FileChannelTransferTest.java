package Java.io.channel;

/**
 * @author XYC
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Semaphore;

/**
 * 启动一个TCP服务端和TCP客户端，客户端从服务端下载一个视频文件
 * 这个文件必须先存在于当前目录
 */
public class FileChannelTransferTest {
    public static void main(String[] args) throws InterruptedException, IOException {
        // 使用一个信号量确保 Server 比 Client 先启动
        Semaphore semaphore = new Semaphore(1);
        // 额外开一个线程运行
        startServer(semaphore);
        // 运行在主线程
        startClient(semaphore);
    }

    /**
     * 客户端代码
     * */
    static void startClient(Semaphore semaphore) throws InterruptedException, IOException {
        // 等待 server 释放资源
        semaphore.acquire();
        // 打开一个 Socket 通道，并连接到服务端
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 8080));
        System.out.println("Client Started.");
        // 释放资源
        semaphore.release();
        // 打开文件通道
        FileChannel fileChannel = FileChannel.open(Paths.get("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io\\World.txt"),
                StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE,                                                                          StandardOpenOption.CREATE);
        // 将 socket 通道的数据转到文件通道
        fileChannel.transferFrom(socketChannel, 0, Long.MAX_VALUE);
        // 确保数据刷出到 I/O 设备
        fileChannel.force(false);
        fileChannel.close();
        socketChannel.close();
        semaphore.release();
    }

    /**
     * 服务端代码
     */
    static void startServer(Semaphore semaphore) throws InterruptedException {
        semaphore.acquire();
        new Thread(()->{
            try{
                // 打开服务端通道
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                // 绑定 8080 端口
                serverSocketChannel.bind(new InetSocketAddress(8080));
                System.out.println("Server Started.");
                semaphore.release();
                // 等待客户端连接
                SocketChannel clientChannel = serverSocketChannel.accept();
                // 以只读的方式打开文件通道
                FileChannel fileChannel = FileChannel.open(Paths.get("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io\\Hello.txt"), StandardOpenOption.READ);

                // 循环调用 transferTo，确保数据传输完整
                long transferred = 0;
                while (transferred < fileChannel.size()){
                    transferred += fileChannel.transferTo(transferred, fileChannel.size(), clientChannel);
                }

                fileChannel.close();
                clientChannel.close();
                serverSocketChannel.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
