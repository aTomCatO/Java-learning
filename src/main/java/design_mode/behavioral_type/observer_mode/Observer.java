package design_mode.behavioral_type.observer_mode;

import design_mode.behavioral_type.observer_mode.content.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import design_mode.behavioral_type.observer_mode.subject.Subject;


/**
 * @author XYC
 * 观察者接口
 */
public interface Observer {

    /**
     * 订阅主题
     *
     * @param subject 主题对象
     */
    void subscribe(Subject subject);

    /**
     * 接收通知
     *
     * @param content 通知内容
     */
    void receive(Content content);

    /**
     * Observer Name
     *
     * @return Observer Name
     */
    String getName();
}
