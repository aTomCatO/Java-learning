package Util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author XYC
 */
public class FileUtils {

    public static InputStream readDiscFile(String filePath) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream readResourceFile(String filePath) {
        try (InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath)) {
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载配置文件
     *
     * @param filePath 文件路径
     * @return Properties
     */
    public static Properties configLoad(String filePath) {
        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(Paths.get(filePath));
        } catch (IOException e) {
            inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        }
        if (inputStream == null) {
            throw new RuntimeException("【ERROR】文件路径异常！filePath:" + filePath);
        }
        Properties properties;
        try (InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties = new Properties();
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

    /**
     * 读取 key value 内容形式的配置文件的做映射
     *
     * @param filePath  resources下的文件路径
     * @param separator key value 分隔符
     * @return Map
     */
    public static Map<String, String> configMapped(String filePath, String separator) {
        List<String> lines = fileContentList(filePath);
        if (lines != null) {
            Map<String, String> map = new HashMap<>(6);
            for (String line : lines) {
                // 将 每行（line） 分割为 key 和 value
                String[] kv = line.split(separator);
                map.put(kv[0], kv[1]);
            }
            return map;
        }
        return null;
    }

    /**
     * 读取 resources下的文件内容以 list 的方式返回
     * 注意：只能获取类路径下的资源
     * <p>
     * resources下的文件是存在于jar这个文件里面，在磁盘上是没有真实路径存在的，它是位于jar内部的一个路径。
     *
     * @param filePath resources下的文件路径
     * @return list 文件的每行数据
     */
    public static List<String> fileContentList(String filePath) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(
                        FileUtils.class.getClassLoader().getResourceAsStream(filePath)), StandardCharsets.UTF_8))) {
            return reader.lines().toList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将本地文件转为字节流
     *
     * @param filePath 本地文件路径
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] fileToBytes(String filePath) throws IOException {
        File file = new File(filePath);
        FileInputStream fileInputStream = new FileInputStream(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        fileInputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }


    /**
     * 将网络文件转为字节流
     *
     * @param urlString 网络文件URL
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] networkFileToBytes(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        InputStream inputStream = connection.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, length);
        }
        inputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }
}
