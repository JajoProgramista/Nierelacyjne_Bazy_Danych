package pl.lodz.p.info.infrastructure.redisAdapter;

public class RoomRedisEnt {
    private int number;
    private int capacity;
    private double basePrice;
    private int isRented;

    public RoomRedisEnt() {
    }

    public RoomRedisEnt(int number, int capacity, double basePrice, int isRented) {
        this.number = number;
        this.capacity = capacity;
        this.basePrice = basePrice;
        this.isRented = isRented;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public int getIsRented() {
        return isRented;
    }

    public void setIsRented(int isRented) {
        this.isRented = isRented;
    }
}
