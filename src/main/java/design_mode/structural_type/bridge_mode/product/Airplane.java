package design_mode.structural_type.bridge_mode.product;

import design_mode.structural_type.bridge_mode.maker.AirplaneMaker;

/**
 * @author XYC
 */
public abstract class Airplane {
    protected AirplaneMaker maker;

    /**
     * 产品
     */
    public abstract void product();
}
