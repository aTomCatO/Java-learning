package design_mode.behavioral_type.memento_mode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import design_mode.behavioral_type.Hero;

import java.util.HashMap;
import java.util.Map;

/**
 * @author XYC
 * 备忘录模式
 * 在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，
 * 这样就可以在以后将对象恢复到原先保存的状态。
 */
public class HeroRecorder {
    private final Map<String, String> heroMap = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Hero getRecord(String heroName) {
        try {
            String heroStr = heroMap.get(heroName);
            return objectMapper.readValue(heroStr, Hero.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void addRecord(Hero hero) {
        try {
            heroMap.put(hero.getName(), objectMapper.writeValueAsString(hero));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
