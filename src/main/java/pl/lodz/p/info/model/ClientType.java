package pl.lodz.p.info.model;

public interface ClientType {
    double applyDiscount(double price);
    String getInfo();
    String clientTypeToString();
}