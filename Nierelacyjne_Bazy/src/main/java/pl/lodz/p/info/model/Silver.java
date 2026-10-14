package pl.lodz.p.info.model;

public class Silver implements ClientType {
    @Override
    public double applyDiscount(double price) {
        return 0.05 * price;
    }

    @Override
    public String getInfo() {
        return "SREBRNY, 5% ZNIZKI";
    }

    @Override
    public String clientTypeToString() {
        return "s";
    }
}