package pl.lodz.p.info.model;

public class Gold implements ClientType {
    @Override
    public double applyDiscount(double price) {
        return 0.1 * price;
    }

    @Override
    public String getInfo() {
        return "ZLOTY, 10% ZNIZKI";
    }

    @Override
    public String clientTypeToString() {
        return "g";
    }
}