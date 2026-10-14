package pl.lodz.p.info.model;

public class Room {
    private final int number;
    private final int capacity;
    private double basePrice;

    public Room(int number, int capacity, double basePrice) {
        if (number < 0) throw new IllegalArgumentException("Negative room number");
        if (basePrice <= 0) throw new IllegalArgumentException("Negative or Equal to 0 Base Price");
        if (capacity <= 0) throw new IllegalArgumentException("Capacity is negative or equals to 0");

        this.number = number;
        this.capacity = capacity;
        this.basePrice = basePrice;
    }

    public int getNumber() { return number; }
    public int getCapacity() { return capacity; }
    public double getBasePrice() { return basePrice; }

    public void setBasePrice(double basePrice) {
        if (basePrice >= 0) this.basePrice = basePrice;
    }

    public String getInfo() {
        return "POKOJ NUMER " + number + "\n" +
                "POJEMNOSC DLA " + capacity + " OSOB\n" +
                "CENA ZA DOBE HOTELOWA (OD GODZINY 12:00 DO GODZINY 12:00) :" + basePrice + "\n";
    }
}
