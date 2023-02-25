package Java.io.nio.buffer;

import org.junit.Test;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Scanner;


/**
 * @author XYC
 */
public class MappedByteBufferTest {

    /**
     * 将共享内存和磁盘文件建立联系的是文件通道类：FileChannel。
     * 该类的加入是JDK为了统一对外部设备（文件、网络接口等）的访问方法，并且加强了多线程对同一文件进行存取的安全性。
     * 这里只是用它来建立共享内存用，它建立了共享内存和磁盘文件之间的一个通道。
     * FileChannel提供了map方法把文件映射到虚拟内存，通常情况可以映射整个文件，如果文件比较大，可以进行分段映射。
     * 大致的步骤：
     * 1. 首先通过 RandomAccessFile获取文件通道 FileChannel（或通过FileChannel的open方法来获取）。
     * 2. 然后，通过 FileChannel 进行内存映射，获取一个虚拟内存区域 VMA。
     * <properties>
     * FileChannel #map方法的参数：
     * 1、映射类型：
     * MapMode mode：内存映像文件访问的方式，FileChannel中的几个常量定义，共三种：
     * MapMode.READ_ONLY：只读，试图修改得到的缓冲区将导致抛出异常。
     * MapMode.READ_WRITE：读/写，对得到的缓冲区的更改最终将写入文件；但该更改对映射到同一文件的其他程序不一定是可见的。
     * MapMode.PRIVATE：私用，可读可写,但是修改的内容不会写入文件，只是buffer自身的改变，这种能力称之为”copy on write”。
     * 2、position：文件映射时的起始位置。
     * 3、length：映射区的长度。长度单位为字节。长度单位为字节
     */
    Logger logger = LoggerFactory.getLogger(MappedByteBufferTest.class);

    @Test
    public void test() {
        File file = new File("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io\\Hello.txt");
        long len = file.length();
        byte[] bytes = new byte[(int) len];
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");) {
            MappedByteBuffer mappedByteBuffer =
                    randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, len);
            for (int i = 0; i < len; i++) {
                byte b = mappedByteBuffer.get();
                bytes[i] = b;
            }

            Scanner scan = new Scanner(new ByteArrayInputStream(bytes)).useDelimiter(" ");
            while (scan.hasNext()) {
                System.out.print(scan.next() + " ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void writeFileDataBuffered() throws IOException {
        File file = File.createTempFile("dbf", ".txt", new File("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io"));
        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(Files.newOutputStream(file.toPath())));

        long counter = 1;
        long numberOfRows = 1000000 * 100;

        long startTime = System.currentTimeMillis();

        do {
            out.writeBytes(counter + System.lineSeparator());
            counter++;
        } while (counter <= numberOfRows);

        out.close();
        long endTime = System.currentTimeMillis();
        System.out.println("Time(s): " + (endTime - startTime) / 1000f);
    }
}