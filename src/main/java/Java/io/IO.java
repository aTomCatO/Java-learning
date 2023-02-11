package Java.io;

import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.nio.charset.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class IO {
    @Test
    /**一、利用通道完成文件的复制(非直接缓冲区。效率低)*/
    public void test1() throws IOException {
        FileInputStream in = new FileInputStream("D:/JavaWorld/Demo/Java/src/main/java/Java/NIO/Hallo.txt");
        FileOutputStream out = new FileOutputStream("D:/JavaWorld/Demo/Java/src/main/java/Java/NIO/World.txt");
        //1、获取通道
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();
        //2、分配指定大小的缓冲区
        ByteBuffer b = ByteBuffer.allocate(666);
        //3、将通道中的数据读入缓冲区中
        while (inChannel.read(b) != -1) {
            b.flip(); //切换读数据模式
            //4、将缓冲区中的数据写入通道中
            outChannel.write(b);
            b.clear(); //清空缓冲区
        }
        outChannel.close();
        inChannel.close();
        in.close();
        out.close();
    }

    /**
     * 二、使用直接缓冲区完成文件的复制(内存映射文件。效率高，只支持ByteBuffer，不安全，消耗多，，)
     */
    public static void test2() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("D:/", "Ha.java"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:/", "Ha"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //直接缓冲区通过allocateDirect(),map()方法来创建
        //内存映射文件
        MappedByteBuffer inMappedBb = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappedBb = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接对缓冲区进行数据的读写操作
        byte[] b = new byte[inMappedBb.limit()];
        inMappedBb.get(b);
        outMappedBb.get(b);

        inChannel.close();
        outChannel.close();
    }

    /**
     * 三、通道之间的数据传输(直接缓冲区)
     * transferFrom() 、 transferTo()
     */
    public static void test3() throws IOException {
        FileChannel inChannel = FileChannel.open(Paths.get("D:/Ha.java"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("D:/Ha"), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //inChannel.transferTo(0,inChannel.size(),outChannel);
        outChannel.transferFrom(inChannel, 0, inChannel.size());

        inChannel.close();
        outChannel.close();
    }

    /**
     * 四、分散(Scatter) 与 聚集(Gather)
     * 分散读取：将通道中的数据分散到多个缓冲区中
     * 聚集写入：将多个缓冲区中的数据聚集到通道中
     */
    public static void test4() throws IOException {
        RandomAccessFile raf1 = new RandomAccessFile("D:/Ha.java", "rw");

        //1、获取通道
        FileChannel f0 = raf1.getChannel();

        //2、分配指定大小的缓冲区
        ByteBuffer b0 = ByteBuffer.allocate(666);
        ByteBuffer b1 = ByteBuffer.allocate(888);

        //3、分散读取
        ByteBuffer[] bs = {b0, b1};
        f0.read(bs);

        for (ByteBuffer byteBuffer : bs) {
            byteBuffer.flip();
        }
        System.out.println(new String(bs[0].array(), 0, bs[0].limit()));
        System.out.println("------------");
        System.out.println(new String(bs[1].array(), 0, bs[1].limit()));

        //4、聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("D:/Ha.java", "rw");
        FileChannel f1 = raf2.getChannel();

        f1.write(bs);
        f0.close();
        f1.close();
    }

    /**
     * 五、字符集：Charset(1、编码：字符串-》字节数组；2、解码：字节数组-》字符串)
     */
    public static void test5() throws IOException {
        Charset c1 = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder ce = c1.newEncoder();
        //获取解码器
        CharsetDecoder cd = c1.newDecoder();

        CharBuffer cb1 = CharBuffer.allocate(666);
        Scanner s = new Scanner(System.in);
        String a;
        cb1.put(a = s.next());
        //char[] c = a.toCharArray();

        //编码
        cb1.flip();
        ByteBuffer b = ce.encode(cb1);
        for (int i = 0; i <= a.length(); i++) {
            System.out.println(b.get());
        }
        //解码
        b.flip();
        System.out.println("---------");
        Charset c2 = Charset.forName("GBK");
        CharBuffer cb2 = c2.decode(b);
        System.out.println(cb2.toString());
    }

    //网络NIO:  1、客户端test6（）；  2、服务端test7（）.
    //         3、选择器（Selector）：是SelectableChannel的多路复用器。用于监听SelectableChannel的IO情况
    // 获取网络通道.
    static SocketChannel sc;
    static FileChannel inChannel;
    static FileChannel outChannel;
    static ServerSocketChannel ssc;

    static {
        try {
            sc = SocketChannel.open(new InetSocketAddress("10.2.20.74", 49666));
            inChannel = FileChannel.open(Paths.get("D:/Ha.java"), StandardOpenOption.READ);
            outChannel = FileChannel.open(Paths.get("D:/Ha"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
            ssc = ServerSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void test6() throws IOException {
        ByteBuffer b = ByteBuffer.allocate(666);
        //读取本地文件，并发送到服务端
        while (inChannel.read(b) != -1) {
            b.flip();
            sc.write(b);
            b.clear();
        }
        inChannel.close();
        sc.close();
    }

    public static void test7() throws IOException {
        ServerSocketChannel sc = ServerSocketChannel.open();
        //绑定连接
        try {
            sc.bind(new InetSocketAddress(49666));
            //获取客户端连接的通道
            SocketChannel skc = sc.accept();

            ByteBuffer b = ByteBuffer.allocate(888);
            //接收客户端的数据，并保存到本地
            while (skc.read(b) != -1) {
                b.flip();
                outChannel.write(b);
                b.clear();
            }
            skc.close();
            outChannel.close();
            sc.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //阻塞型网络IO.客户端：test8； 服务端：test9
    public static void test8() throws IOException {
        ByteBuffer b = ByteBuffer.allocate(666);
        while (inChannel.read(b) != -1) {
            b.flip();
            sc.write(b);
            b.clear();
        }
        sc.shutdownInput();
        //接收服务端的反馈
        int len;
        while ((len = sc.read(b)) != -1) {
            b.flip();
            System.out.println(new String(b.array(), 0, len));
            b.clear();
        }
        inChannel.close();
        sc.close();
    }

    public static void test9() throws IOException {
        ssc.bind(new InetSocketAddress(57000));
        //获取客户端连接的通道
        SocketChannel sc = ssc.accept();
        ByteBuffer b = ByteBuffer.allocate(888);
        //接收客户端的数据，并保存到本地
        while (sc.read(b) != -1) {
            b.flip();
            outChannel.write(b);
            b.clear();
        }
        //发送反馈给客户端
        b.put("服务端接收数据成功".getBytes());
        b.flip();
        sc.write(b);
        sc.close();
        outChannel.close();
        sc.close();
    }

    //非阻塞式IO
    public static void test10() throws IOException {   //客户端
        //切换成非阻塞模式
        sc.configureBlocking(false);
        ByteBuffer b = ByteBuffer.allocate(666);
        b.put(new Date().toString().getBytes());
        b.flip();
        sc.write(b);
        b.clear();
        sc.close();
    }

    public static void test11() throws IOException {  //服务端
        ssc.configureBlocking(false);
        //绑定连接
        ssc.bind(new InetSocketAddress(9898));
        //获取选择器
        Selector se = Selector.open();
        //将通道注册到选择器上,并且指定“监听接收事件”
        ssc.register(se, SelectionKey.OP_ACCEPT);
        //轮询式的获取选择器上已经“准备就绪”的事件
        while (se.select() > 0) {
            //获取当前选择器中所有注册的“选择键（已就绪的监听事件）”
            Iterator<SelectionKey> it = se.selectedKeys().iterator();
            while (it.hasNext()) {
                //获取准备“就绪”的事件
                SelectionKey sk = it.next();
                //判断具体是什么事件准备就绪
                if (sk.isAcceptable()) {
                    //若“接收就绪”，获取客户端连接
                    SocketChannel sc = ssc.accept();
                    //切换非阻塞模式
                    sc.configureBlocking(false);
                    //将该通道注册到选择器上
                    sc.register(se, SelectionKey.OP_READ);
                } else if (sk.isReadable()) {
                    //获取当前选择器上“读就绪”状态的通道
                    SocketChannel sc = (SocketChannel) sk.channel();
                    //读取数据
                    ByteBuffer b = ByteBuffer.allocate(888);
                    int len = 0;
                    while ((len = sc.read(b)) > 0) {
                        b.flip();
                        System.out.println(new String(b.array(), 0, len));
                        b.clear();
                    }
                }
                //取消选择键SelectionKey
                it.remove();
            }
        }
    }
}

