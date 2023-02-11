package design_mode.behavioral_type.observer_mode.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author XYC
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherContent extends Content {
    private String weather;

    /**
     * 温度
     */
    private String temperature;

    public WeatherContent(String title, String weather, String temperature) {
        this.title = title;
        this.weather = weather;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "最新天气报道 ———— " + title + "\n天气: " + weather + "\n温度： " + temperature;
    }

}
