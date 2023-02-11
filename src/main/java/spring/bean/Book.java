package spring.bean;

import org.springframework.stereotype.Component;

/**
 * @author XYC
 */
@Component
public class Book{
    public Book() {
        System.out.println("construction Book");
    }
}
