package spring;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import spring.bean.Student;

/**
 * @author XYC
 */
public class AutowireModuleTest extends ApplicationTest {
    /**
     * 探究 Spring 传统的自动装配原理（不用@Autowired、@Resource注解的方式）
     * 主要源码看 AbstractAutowireCapableBeanFactory 中的 populateBean方法
     */
    @Test
    public void test1() {
        ClassPathXmlApplicationContext ioc = new ClassPathXmlApplicationContext("Spring.xml");
        Student student = ioc.getBean("student", Student.class);
        System.out.println("这个 student 对象的 teacher 属性值为 null : " + (student.getTeacher() == null));
        ioc.close();
    }
    private void test2(){

    }
}
