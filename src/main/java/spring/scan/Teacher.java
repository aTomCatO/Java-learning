package spring.scan;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;
import spring.event.TaskEvent;

/**
 * @author XYC
 */
@Service
public class Teacher implements ApplicationEventPublisherAware {
    private TaskEvent task;
    private ApplicationEventPublisher applicationEventPublisher;

    public void setTask(TaskEvent task) {
        this.task = task;
    }

    public void publicEvent() {
        applicationEventPublisher.publishEvent(task);
    }

    public Teacher() {
        System.out.println("construction Teacher");
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
