package design_mode.behavioral_type.observer_mode.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XYC
 */
@Data
public class MoodContent extends Content {
    private String moodState;

    public MoodContent(String title, String moodState) {
        this.title = title;
        this.moodState = moodState;
    }

    @Override
    public String toString() {
        return "系统检测到您的情绪发生变化：\n" + moodState;
    }
}
