package design_mode.behavioral_type.state_mode.state;

import design_mode.behavioral_type.Hero;
import design_mode.behavioral_type.state_mode.ability.BattleAbility;

/**
 * @author XYC
 */
public abstract class State implements BattleAbility {
    public String stateName;

    /**
     * hero 状态处理
     *
     * @param hero Hero对象
     */
    public abstract void handle(Hero hero);
}
