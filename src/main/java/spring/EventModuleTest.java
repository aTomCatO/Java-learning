package spring;

import org.junit.Test;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import spring.bean.Book;
import spring.bean.Student;
import spring.event.TaskEvent;
import spring.scan.Teacher;

/**
 * @author XYC
 */
public class EventModuleTest extends ApplicationTest {
    /**
     * 事件机制
     */
    @Test
    public void test1() {
        genericApplicationContext.registerBean("student", Student.class);
        genericApplicationContext.registerBean("book", Book.class);
        genericApplicationContext.registerBean("teacher", Teacher.class);

        defaultListableBeanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());
        genericApplicationContext.registerBean(AutowiredAnnotationBeanPostProcessor.class);

        genericApplicationContext.registerBean(CommonAnnotationBeanPostProcessor.class);

        genericApplicationContext.refresh();

        Teacher teacher = genericApplicationContext.getBean("teacher", Teacher.class);
        Student student = genericApplicationContext.getBean("student", Student.class);


        teacher.setTask(new TaskEvent(this, "古诗背诵", "《清平调》"));
        teacher.publicEvent();


        genericApplicationContext.close();
    }
}
