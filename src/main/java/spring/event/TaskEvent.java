package spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author XYC
 */
public class TaskEvent extends ApplicationEvent {
    private final String taskName;
    private final String taskContent;

    public TaskEvent(Object source, String taskName, String taskContent) {
        super(source);
        this.taskName = taskName;
        this.taskContent = taskContent;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskContent() {
        return taskContent;
    }
}
