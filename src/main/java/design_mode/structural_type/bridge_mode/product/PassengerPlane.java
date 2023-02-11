package design_mode.structural_type.bridge_mode.product;

import design_mode.structural_type.bridge_mode.maker.AirplaneMaker;

public class PassengerPlane extends Airplane {
    public PassengerPlane(AirplaneMaker maker) {
        this.maker = maker;
    }

    @Override
    public void product() {
        System.out.print("我是客机 ");
        maker.produce();
    }
}
