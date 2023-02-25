package Java.io.nio.channel;

import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author XYC
 */
public class FileChannelTest {


    /**
     * 用 FileChannel 进行文件复制
     * 先分别创建了两个文件的输入输出流，
     * 然后在分别获取到两个文件的文件通道，
     * 然后将源文件的文件通道直接和目标文件的文件通道进行连接，
     * 直接将数据写入到目标文件中区,不需要进行分别的读取和写入操作了。
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     */
    public static void fileCopyWithFileChannel(File fromFile, File toFile) {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutput = null;
        try {
            fileInputStream = new FileInputStream(fromFile);
            fileOutputStream = new FileOutputStream(toFile);
            // 得到fileInputStream的文件通道
            fileChannelInput = fileInputStream.getChannel();
            // 得到fileOutputStream的文件通道
            fileChannelOutput = fileOutputStream.getChannel();
            // 将fileChannelInput通道的数据，写入到fileChannelOutput通道
            fileChannelInput.transferTo(0, fileChannelInput.size(), fileChannelOutput);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (fileChannelInput != null)
                    fileChannelInput.close();
                if (fileOutputStream != null)
                    fileOutputStream.close();
                if (fileChannelOutput != null)
                    fileChannelOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从 FileChannel 读数据
     */
    @Test
    public void readTest() {
        FileChannel channel = null;
        try {
            // StandardOpenOption.READ 以只读的方式打开文件的通道
            channel = FileChannel.open(
                    Paths.get("D:\\JavaWorld\\Demo\\EnglishApp\\src\\main\\resources\\EnglishAppData.properties"),
                    StandardOpenOption.READ);

            ByteBuffer buffer = ByteBuffer.allocate(6);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                System.out.print(new String(buffer.array()));
                buffer.clear();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void writeTest() {
        FileChannel channel = null;
        try {
            channel = FileChannel.open(Paths.get("文件路径"), StandardOpenOption.WRITE);

            ByteBuffer buffer = ByteBuffer.allocate(6);
            byte[] writeData = "Hello, Java NIO".getBytes();

            for (int i = 0; i < writeData.length; ) {
                buffer.put(writeData, i, Math.min(writeData.length - i, buffer.limit() - buffer.position()));
                buffer.flip();
                i += channel.write(buffer);
                buffer.compact();
            }
            // 将数据刷出到磁盘，但不包括元数据
            channel.force(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();
        FileChannelTest.fileCopyWithFileChannel(new File("D:\\programmer\\Book\\Java类\\JavaEE\\Tomcat架构解析 (1).pdf"), new File("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io\\World.txt"));
        long endTime = System.currentTimeMillis();
        System.out.println("Time(s): " + (endTime - startTime) / 1000f);
    }


}
