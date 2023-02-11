package Java.http;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

/**
 * @author XYC
 */
public class HttpTest {
    public static void main(String[] args) {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());

        try {
            HtmlPage page = webClient.getPage("https://www.baidu.com/");
            HtmlForm form = page.getFormByName("f");
            HtmlInput textField = form.getInputByName("wd");
            HtmlSubmitInput submitButton = (HtmlSubmitInput) page.getElementById("su");
            textField.setValueAttribute("java");
            HtmlPage newPage = submitButton.click();
            System.out.println(newPage.asNormalizedText());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            webClient.close();
        }
    }
}
