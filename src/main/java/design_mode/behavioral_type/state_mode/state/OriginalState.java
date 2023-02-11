package design_mode.behavioral_type.state_mode.state;

import design_mode.behavioral_type.Hero;
import design_mode.behavioral_type.memento_mode.HeroRecorder;

/**
 * @author XYC
 * 原先的状态
 * 备忘录模式实现
 */
public class OriginalState extends State {

    {
        stateName = "初始状态";
    }

    private static final HeroRecorder HERO_RECORDER = new HeroRecorder();
    public void record(Hero hero) {
        HERO_RECORDER.addRecord(hero);
    }

    @Override
    public void handle(Hero hero) {
        System.out.println(hero.getName() + " 恢复正常");
        hero.setAttack(HERO_RECORDER.getRecord(hero.getName()).getAttack());
        hero.setDefense(HERO_RECORDER.getRecord(hero.getName()).getDefense());
        System.out.println(hero);
    }


    @Override
    public void attackAbility() {
        System.out.println("正常攻击");
    }

    @Override
    public void defenseAbility() {
        System.out.println("正常防御");
    }
}
