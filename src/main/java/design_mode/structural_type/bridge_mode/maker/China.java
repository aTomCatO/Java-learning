package design_mode.structural_type.bridge_mode.maker;

public class China extends AirplaneMaker{
    @Override
    public void produce() {
        System.out.println("中国制造");
    }
}
