package pl.lodz.p.info.model;

public class DefaultClientType implements ClientType {
    @Override
    public double applyDiscount(double price) {
        return 0;
    }

    @Override
    public String getInfo() {
        return "ZWYKLY, BRAK ZNIZKI";
    }

    @Override
    public String clientTypeToString() {
        return "d";
    }
}