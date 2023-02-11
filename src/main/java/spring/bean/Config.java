package spring.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author XYC
 */
@Configuration
@ComponentScan("spring.scan")
public class Config {
    public Config() {
        System.out.println("construction Config");
    }

    @Bean("student")
    public Student getStudent() {
        return new Student();
    }

    @Bean("book")
    public Book getBook() {
        return new Book();
    }


}
