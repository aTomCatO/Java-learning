package Java.io.bio.stream;

import java.io.*;
import java.nio.file.Files;

/**
 * @author XYC
 */
public class StreamTest {
    /**
     * 普通的文件复制方法
     * 传入源文件和目标文件两个参数，
     * 然后根据两个文件，分别出具输入输出流，
     * 然后将输入流的数据读取，并且写入输出流中，
     * 就完成了文件的复制操作。
     *
     * @param fromFile 源文件
     * @param toFile   目标文件
     * @throws FileNotFoundException 未找到文件异常
     */
    public static void fileCopyWithStream(File fromFile, File toFile) throws FileNotFoundException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(Files.newInputStream(fromFile.toPath()));
            outputStream = new BufferedOutputStream(Files.newOutputStream(toFile.toPath()));
            byte[] bytes = new byte[1024];
            int i;
            // 读取到输入流数据，然后写入到输出流中去，实现复制
            while ((i = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (outputStream != null)
                    outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
