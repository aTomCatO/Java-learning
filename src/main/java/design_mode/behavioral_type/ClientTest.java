package design_mode.behavioral_type;

import design_mode.behavioral_type.observer_mode.content.Content;
import design_mode.behavioral_type.observer_mode.content.WeatherContent;
import design_mode.behavioral_type.observer_mode.subject.Subject;
import org.reflections.Reflections;
import design_mode.behavioral_type.state_mode.state.State;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author XYC
 */
public class ClientTest {

    public static final Map<String, State> STATE_MAP;
    public static final Map<String, Subject> SUBJECT_MAP;

    static {
        //获取state包下所有 state抽象类 的子类
        Reflections stateReflections = new Reflections("design_mode/behavioral_type/state_mode/state");
        Set<Class<? extends State>> stateSet = stateReflections.getSubTypesOf(State.class);
        STATE_MAP = new HashMap<>(stateSet.size());
        stateSet.forEach(new Consumer<Class<? extends State>>() {
            @Override
            public void accept(Class<? extends State> aClass) {
                try {
                    Field stateName = aClass.getField("stateName");
                    State state = aClass.newInstance();
                    STATE_MAP.put((String) stateName.get(state), state);
                } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //获取subject包下所有 subject抽象类 的子类
        Reflections subjectReflections = new Reflections("design_mode/behavioral_type/observer_mode/subject");
        Set<Class<? extends Subject>> subjectSet = subjectReflections.getSubTypesOf(Subject.class);
        SUBJECT_MAP = new HashMap<>(subjectSet.size());
        subjectSet.forEach((aClass -> {
            try {
                Field subjectName = aClass.getField("subjectName");
                Subject subject = aClass.newInstance();
                SUBJECT_MAP.put((String) subjectName.get(subject), subject);
            } catch (NoSuchFieldException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public static void main(String[] args) {
        Hero tom = new Hero("tom", 100, 160);
        System.out.println(tom);
        tom.subscribe(SUBJECT_MAP.get("气候主题"));
        tom.subscribe(SUBJECT_MAP.get("情绪主题"));

        tom.stateChange(STATE_MAP.get("生气状态"));
        tom.attackAbility();
        tom.stateChange(STATE_MAP.get("伤心状态"));
        tom.attackAbility();
        tom.stateChange(STATE_MAP.get("初始状态"));
        tom.attackAbility();


        Content content = new WeatherContent("天冷加衣","多雨","16℃");
        SUBJECT_MAP.get("气候主题").updateSubjectContent(content).notifyObserver();
    }
}
