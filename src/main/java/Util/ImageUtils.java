package Util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author XYC
 */
public class ImageUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageUtils.class);

    /**
     * 不能指定压缩大小，压缩不会丢失精度，压缩后的图片很清晰
     */
    public static byte[] compress(byte[] imageBytes) {
        //获取文件输入、输出流
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // 读取原图
            BufferedImage srcImage = ImageIO.read(inputStream);

            // 创建一个新地缩略图
            BufferedImage newImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = newImage.createGraphics();
            graphics.drawImage(srcImage, 0, 0, Color.WHITE, null);

            // 输出为JPG格式的文件
            ImageIO.write(newImage, "jpg", outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}




