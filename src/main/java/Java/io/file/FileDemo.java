package Java.io.file;

import lombok.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author XYC
 */
@Data
public class FileDemo {
    private final Pattern compile = Pattern.compile(Matcher.quoteReplacement(File.separator));

    /**
     * 文件目录层级打印
     * src  -- source 源
     * dest -- destination 目的地
     */
    public void showFile(File srcFile) {
        Matcher matcher = compile.matcher(srcFile.getAbsolutePath());
        int srcFilePathSeparatorCount = 0;
        while (matcher.find()) {
            srcFilePathSeparatorCount++;
        }
        showFile(srcFile, srcFilePathSeparatorCount);
    }

    private void showFile(File srcFile, int srcFilePathSeparatorCount) {
        File[] files = srcFile.listFiles();

        assert files != null;
        for (File f : files) {
            //获取子文件绝对路径的分隔符数量
            String childPath = f.getAbsolutePath();
            Matcher matcher = compile.matcher(childPath);
            int childPathSeparatorCount = 0;
            while (matcher.find()) {
                childPathSeparatorCount++;
            }
            //根据子文件的路径分隔符数 减去 源文件的路径分隔符数 得出该子文件相对于源文件的层级位置
            int spaceCount = childPathSeparatorCount - srcFilePathSeparatorCount;
            StringBuilder stringBuilder = new StringBuilder();
            while ((spaceCount--) != 0) {
                stringBuilder.append("   ");
            }
            stringBuilder.append("|——").append(f.getName());
            System.out.println(stringBuilder);

            //如果这个文件是目录的话，打印这个文件目录的子文件
            if (!f.isFile()) {
                showFile(f, srcFilePathSeparatorCount);
            }
        }
    }

    /**
     * 计算文件大小，单位 字节
     */
    public int computeFileSize(File file) {
        int size = 0;
        File[] files = file.listFiles();
        assert files != null;
        for (File f : files) {
            if (f.isFile()) {
                size += f.length();
            } else {
                size += computeFileSize(f);
            }
        }
        return size;
    }

    /**
     * 判断后缀名
     * */
    public static boolean isSuffix(String fileName, String... suffixes) {

        for (String suffix : suffixes) {
            if (fileName.indexOf(suffix) > -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用工具类实现复制文件的操作
     * <dependency>
     * <groupId>commons-io</groupId>
     * <artifactId>commons-io</artifactId>
     * <version>2.11.0</version>
     * </dependency>
     */
    public void copyFile(File srcFile, File destFile) {
        try {

            FileUtils.copyFile(srcFile, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 自己实现复制操作
     */
    public void myCopyFile(File srcFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileInputStream fileInputStream = new FileInputStream(srcFile);
        FileOutputStream fileOutputStream = new FileOutputStream(destFile);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fileInputStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, length);
        }


        fileInputStream.close();
        fileOutputStream.close();
    }

    public void myCopyDirectory(File srcDir, File destDir) {
        if (!destDir.exists()) {
            destDir.mkdir();
        }

        File copyDirectory = new File(destDir, srcDir.getName());
        copyDirectory.mkdir();

        File[] files = srcDir.listFiles();
        assert files != null;
        for (File file : files) {
            if (file.isDirectory()) {
                myCopyDirectory(file, copyDirectory);
            } else {
                try {
                    myCopyFile(file, new File(copyDirectory, file.getName()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }


    public static void main(String[] args) throws IOException {
        new FileDemo().myCopyDirectory(
                new File("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\nio"),
                new File("D:\\JavaWorld\\Demo\\Java\\src\\main\\java\\Java\\a"));
    }
}
