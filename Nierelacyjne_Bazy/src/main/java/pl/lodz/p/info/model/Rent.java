package pl.lodz.p.info.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Rent {
    private final int id;
    private double rentCost;
    private final LocalDateTime beginDate;
    private final LocalDateTime endDate;
    private final Room room;
    private final Client client;
    private boolean isCanceled = false;

    public Rent(int id, LocalDateTime beginDate, LocalDateTime endDate, Room room, Client client) {
        if (client == null) throw new IllegalArgumentException("Invalid Client");
        if (room == null) throw new IllegalArgumentException("Invalid Room");
        if (beginDate == null || endDate == null) throw new IllegalArgumentException("Invalid Date");
        if (beginDate.isAfter(endDate)) throw new IllegalArgumentException("Invalid Date Range");

        this.id = id;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.room = room;
        this.client = client;
        this.rentCost = 0.0;
    }

    public int getId() { return id; }
    public double getRentCost() { return rentCost; }
    public LocalDateTime getBeginDate() { return beginDate; }
    public LocalDateTime getEndDate() { return endDate; }
    public Room getRoom() { return room; }
    public Client getClient() { return client; }
    public boolean getIsCanceled() { return isCanceled; }
    public void setIsCanceled(boolean isCanceled) { this.isCanceled = isCanceled; }

    public int getRentDays() {
        long days = ChronoUnit.DAYS.between(beginDate, endDate);
        return days == 0 ? 1 : (int) days;
    }

    public void endRent() {
        if (!isCanceled) {
            double withoutDiscount = getRentDays() * room.getBasePrice();
            rentCost = withoutDiscount - client.applyDiscount(withoutDiscount);
        }
    }

    private String monthName(int month) {
        String[] names = {"", "STYCZNIA", "LUTEGO", "MARCA", "KWIETNIA", "MAJA", "CZERWCA",
                "LIPCA", "SIERPNIA", "WRZESNIA", "PAZDZIERNIKA", "LISTOPADA", "GRUDNIA"};
        return names[month];
    }

    public String getInfo() {
        StringBuilder ss = new StringBuilder();
        ss.append("--------------------------------------------------------------------\n");
        ss.append("IDENTYFIKATOR WYNAJMU: ").append(id).append("\n");
        ss.append("NUMER POKOJU: ").append(room.getNumber()).append("\n");
        ss.append("OKRES: ").append(beginDate.getDayOfMonth()).append(" ")
                .append(monthName(beginDate.getMonthValue())).append(" ").append(beginDate.getYear()).append("r. - ")
                .append(endDate.getDayOfMonth()).append(" ").append(monthName(endDate.getMonthValue())).append(" ")
                .append(endDate.getYear()).append("r.\n");

        ss.append("LICZBA DOB HOTELOWYCH: ").append(getRentDays()).append("\n");
        if (isCanceled) {
            ss.append("KOSZT WYNAJMU: REZERWACJA ODWOLANA!\n");
        } else if (rentCost == 0) {
            ss.append("KOSZT WYNAJMU: JESZCZE SIE NIE SKONCZYL!\n");
        } else {
            ss.append("KOSZT WYNAJMU: ").append(rentCost).append("\n");
        }
        ss.append("--------------------------------------------------------------------\n");
        return ss.toString();
    }
}