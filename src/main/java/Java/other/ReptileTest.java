package Java.other;

/**
 * <dependency>
 * <groupId>org.jsoup</groupId>
 * <artifactId>jsoup</artifactId>
 * <version>1.14.3</version>
 * </dependency>
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * @author XYC
 * http://www.gaosan.com/gaokao/317035.html
 * https://www.ruiwen.com/word/yingyujingdianjuzi150ju.html
 */
public class ReptileTest {
    public static void main(String[] args) throws IOException {
        String url = "https://www.ruiwen.com/word/yingyujingdianjuzi150ju.html";
        Document document = Jsoup.parse(new URL(url), 30000);
        System.out.println(document.text());
        //获取文档中所有的p元素(标签)
        Elements elements = document.getElementsByTag("properties");
        //遍历所有的p元素,获取每个p元素的文本
        for (Element element : elements) {
            System.out.println(element.text());
        }
    }
}
