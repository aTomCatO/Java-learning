package Java.io.nio.buffer;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author XYC
 */
public class BufferTest {
    /**
     * 分散读
     */
    @Test
    public void test1() {
        ByteBuffer b1 = ByteBuffer.allocate(5);
        ByteBuffer b2 = ByteBuffer.allocate(5);
        ByteBuffer b3 = ByteBuffer.allocate(5);
        ByteBuffer[] byteBuffers = new ByteBuffer[]{b1, b2, b3};
        try (FileInputStream fileInputStream = new FileInputStream("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io\\World.txt")) {
            FileChannel channel = fileInputStream.getChannel();
            channel.read(byteBuffers);
            for (ByteBuffer byteBuffer : byteBuffers) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    System.out.print(((char) byteBuffer.get()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 集中写
     */
    @Test
    public void test2() {
        ByteBuffer b1 = StandardCharsets.UTF_8.encode(" Hello World ");
        ByteBuffer b2 = StandardCharsets.UTF_8.encode(" Hello World ");
        ByteBuffer b3 = StandardCharsets.UTF_8.encode(" Hello World ");
        ByteBuffer[] byteBuffers = new ByteBuffer[]{b1, b2, b3};
        try (FileOutputStream fileOutputStream = new FileOutputStream("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io\\World.txt")) {
            FileChannel channel = fileOutputStream.getChannel();
            channel.write(byteBuffers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
