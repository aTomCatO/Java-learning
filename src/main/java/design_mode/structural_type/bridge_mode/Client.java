package design_mode.structural_type.bridge_mode;

import design_mode.structural_type.bridge_mode.maker.AirplaneMaker;
import design_mode.structural_type.bridge_mode.maker.American;
import design_mode.structural_type.bridge_mode.maker.China;
import design_mode.structural_type.bridge_mode.product.Airplane;
import design_mode.structural_type.bridge_mode.product.CargoPlane;
import design_mode.structural_type.bridge_mode.product.PassengerPlane;

public class Client {
    public static void main(String[] args) {
        AirplaneMaker china = new China();
        Airplane cargoPlane = new CargoPlane(china);
        Airplane passengerPlane = new PassengerPlane(china);
        cargoPlane.product();
        passengerPlane.product();

        System.out.println("********************");

        AirplaneMaker american = new American();
        cargoPlane = new CargoPlane(american);
        passengerPlane = new PassengerPlane(american);
        cargoPlane.product();
        passengerPlane.product();
        StringBuilder stringBuilder = new StringBuilder();
    }
}
