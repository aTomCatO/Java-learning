package design_mode.behavioral_type;

import design_mode.behavioral_type.observer_mode.Observer;
import design_mode.behavioral_type.observer_mode.content.Content;
import design_mode.behavioral_type.observer_mode.subject.Subject;
import design_mode.behavioral_type.state_mode.ability.BattleAbility;
import lombok.Data;
import lombok.NoArgsConstructor;
import design_mode.behavioral_type.state_mode.state.OriginalState;
import design_mode.behavioral_type.state_mode.state.State;

/**
 * @author XYC
 */
@Data
@NoArgsConstructor
public class Hero implements Observer, BattleAbility {

    private String name;
    private State state;
    /**
     * 攻击力
     */
    private Integer attack;
    /**
     * 防御力
     */
    private Integer defense;


    public Hero(String name, Integer attack, Integer defense) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        new OriginalState().record(this);
    }

    public void stateChange(State state) {
        this.state = state;
        this.state.handle(this);
    }

    @Override
    public String toString() {
        return name + "  ——  攻击力：" + attack + "  防御力：" + defense;
    }

    @Override
    public void subscribe(Subject subject) {
        subject.addObserver(this);
    }

    @Override
    public void receive(Content content) {
        System.out.println("******************************");
        System.out.println("收件人： " + name + "\n" + content);
        System.out.println("******************************");
    }

    @Override
    public void attackAbility() {
        state.attackAbility();
    }

    @Override
    public void defenseAbility() {
        state.defenseAbility();
    }
}
