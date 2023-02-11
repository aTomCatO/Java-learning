package design_mode.behavioral_type.state_mode.state;

import design_mode.behavioral_type.Hero;

/**
 * @author XYC
 */
public class AngryState extends State {
    {
        stateName = "生气状态";
    }

    @Override
    public void handle(Hero hero) {
        System.out.println(hero.getName() + " 生气了");
        hero.setAttack(hero.getAttack() + 66);
        hero.setDefense(hero.getDefense() - 66);
        System.out.println(hero);
    }

    @Override
    public void attackAbility() {
        System.out.println("攻击能力提高");
    }

    @Override
    public void defenseAbility() {
        System.out.println("防御能力下降");
    }
}
