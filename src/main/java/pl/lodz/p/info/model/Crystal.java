package pl.lodz.p.info.model;

public class Crystal implements ClientType {
    @Override
    public double applyDiscount(double price) {
        return 0.15 * price;
    }

    @Override
    public String getInfo() {
        return "KRYSTALICZNY, 15% ZNIZKI";
    }

    @Override
    public String clientTypeToString() {
        return "c";
    }
}