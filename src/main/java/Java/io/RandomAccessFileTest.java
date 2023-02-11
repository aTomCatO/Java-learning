package Java.io;

import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author XYC
 */
public class RandomAccessFileTest {
    public static void main(String[] args) throws IOException {
        RandomAccessFileTest.insertContent("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\io\\Hello.txt", 1, "world");
    }

    /**
     * 向文件的指定位置插入内容
     *
     * @param filePath 文件路径
     * @param pos      插入位置
     * @param content  插入内容
     */
    public static void insertContent(String filePath, long pos, String content) throws IOException {
        RandomAccessFile raf = null;
        FileOutputStream tmpOut;
        FileInputStream tmpIn;
        //创建一个临时文件来保存插入点后的数据
        File tempFile = File.createTempFile("tmp", null);
        tempFile.deleteOnExit();
        try {
            raf = new RandomAccessFile(filePath, "rw");
            tmpOut = new FileOutputStream(tempFile);
            tmpIn = new FileInputStream(tempFile);
            raf.seek(pos);
            //--------下面代码将插入点后的内容读入临时文件中保存---------
            byte[] bytes = new byte[64];
            //用于记录实际读取的字节数
            int hasRead;
            //使用循环方式读取插入点后的数据，并将读取的数据写入临时文件
            while ((hasRead = raf.read(bytes)) > 0) {
                tmpOut.write(bytes, 0, hasRead);
            }
            //----------下面代码插入内容----------
            //把文件记录指针重新定位到pos位置
            raf.seek(pos);
            //追加需要插入的内容
            raf.write(content.getBytes());
            //追加临时文件中的内容
            while ((hasRead = tmpIn.read(bytes)) > 0) {
                raf.write(bytes, 0, hasRead);
            }
        } finally {
            if (raf != null) {
                raf.close();
            }
        }
    }
}
