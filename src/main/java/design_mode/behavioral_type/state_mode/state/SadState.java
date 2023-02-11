package design_mode.behavioral_type.state_mode.state;

import design_mode.behavioral_type.ClientTest;
import design_mode.behavioral_type.Hero;
import design_mode.behavioral_type.observer_mode.content.Content;
import design_mode.behavioral_type.observer_mode.content.MoodContent;

/**
 * @author XYC
 * 伤心状态
 */
public class SadState extends State {
    {
        stateName = "伤心状态";
    }

    @Override
    public void handle(Hero hero) {
        System.out.println(hero.getName() + " 伤心了");
        hero.setAttack(hero.getAttack() - 36);
        hero.setDefense(hero.getDefense() - 36);
        System.out.println(hero);

        Content content = new MoodContent("开心点嘛", "伤心了");
        ClientTest.SUBJECT_MAP.get("情绪主题").updateSubjectContent(content).notifyObserver();
    }

    @Override
    public void attackAbility() {
        System.out.println("攻击能力下降");
    }

    @Override
    public void defenseAbility() {
        System.out.println("防御能力下降");
    }
}
