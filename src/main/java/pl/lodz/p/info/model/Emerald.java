package pl.lodz.p.info.model;

public class Emerald implements ClientType {
    @Override
    public double applyDiscount(double price) {
        return 0.2 * price;
    }

    @Override
    public String getInfo() {
        return "SZMARAGDOWY, ZNIZKA 20%";
    }

    @Override
    public String clientTypeToString() {
        return "e";
    }
}