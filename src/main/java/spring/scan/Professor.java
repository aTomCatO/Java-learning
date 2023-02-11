package spring.scan;

import org.springframework.stereotype.Component;

/**
 * @author XYC
 */
@Component
public class Professor {

    public Professor() {
        System.out.println("construction Professor");
    }
}
