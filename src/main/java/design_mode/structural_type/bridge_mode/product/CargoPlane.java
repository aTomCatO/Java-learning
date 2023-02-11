package design_mode.structural_type.bridge_mode.product;

import design_mode.structural_type.bridge_mode.maker.AirplaneMaker;

public class CargoPlane extends Airplane {
    public CargoPlane(AirplaneMaker maker) {
        this.maker = maker;
    }

    @Override
    public void product() {
        System.out.print("我是货机 ");
        maker.produce();
    }
}
