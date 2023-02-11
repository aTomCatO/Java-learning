package design_mode.behavioral_type.observer_mode.subject;

import design_mode.behavioral_type.observer_mode.content.Content;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XYC
 * 天气主题
 */
@Data
public class WeatherSubject extends Subject {
    {
        subjectName = "气候主题";
    }
}
