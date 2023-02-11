package spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import spring.event.TaskEvent;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.scan.Teacher;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author XYC
 */
@Data
@AllArgsConstructor
@Component
public class Student implements People, ApplicationListener<TaskEvent>, BeanNameAware, ApplicationContextAware, InitializingBean {

    private Teacher teacher;

    private Book book;
    private String home;

    public Student() {
        System.out.println("construction Student");
    }

    @Autowired
    public void setBook(Book book) {
        System.out.println("Student: @Autowired依赖注入Book");
        this.book = book;
    }

    @Autowired
    public void setHome(@Value("${java_home}") String home) {
        System.out.println("Student: @Autowired@Value依赖注入home");
        this.home = home;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Student 实现 BeanNameAware 接口：setBeanName " + name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("Student 实现 ApplicationContextAware 接口");
    }

    /**
     * @PostConstruct注解的方法会将在依赖注入完成之后被自动调用。
     */
    @PostConstruct
    public void init() {
        System.out.println("Student @PostConstruct： 初始化");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Student 实现 InitializingBean 接口： 初始化");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Student @PreDestroy： 销毁");
    }

    @Override
    public String number(Number number) {
        System.out.println("Student number");
        return String.valueOf(number);
    }

    @Override
    public void onApplicationEvent(TaskEvent event) {
        System.out.println("Student 实现 ApplicationListener 接口");
        System.out.println("=====================================================================");
        System.out.println("收到老师布置的任务：");
        System.out.println("任务标题：" + event.getTaskName());
        System.out.println("任务内容：" + event.getTaskContent());
        System.out.println("=====================================================================");
    }

}

