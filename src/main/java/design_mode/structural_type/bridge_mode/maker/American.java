package design_mode.structural_type.bridge_mode.maker;

public class American extends AirplaneMaker{
    @Override
    public void produce() {
        System.out.println("美国制造");
    }
}
