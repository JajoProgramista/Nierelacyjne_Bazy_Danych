package pl.lodz.p.info.infrastructure.mongoEntity;

import org.bson.codecs.pojo.annotations.BsonId;
import java.time.LocalDateTime;

public class RentMongoEnt {
    @BsonId
    private int id;
    private double rentCost;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
    private boolean isCanceled;
    private String clientPersonalId;
    private int roomNumber;

    public RentMongoEnt() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getRentCost() {
        return rentCost;
    }

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    public LocalDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(LocalDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public String getClientPersonalId() {
        return clientPersonalId;
    }

    public void setClientPersonalId(String clientPersonalId) {
        this.clientPersonalId = clientPersonalId;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
