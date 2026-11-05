package pl.lodz.p.info.infrastructure.mongoEntity;

import org.bson.codecs.pojo.annotations.BsonId;

public class RoomMongoEnt {
    @BsonId
    private int number;
    private int capacity;
    private double basePrice;

    public RoomMongoEnt() {
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
}
