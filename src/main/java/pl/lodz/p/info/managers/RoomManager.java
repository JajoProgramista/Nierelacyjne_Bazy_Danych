package pl.lodz.p.info.managers;

import pl.lodz.p.info.infrastructure.adapter.JpaRoomRepositoryAdapter;
import pl.lodz.p.info.model.Rent;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.RoomRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RoomManager {
    private final RoomRepository roomRepository = new JpaRoomRepositoryAdapter();

    public Room getRoom(int number) {
        return roomRepository.findByNumber(number);
    }

    public Room registerRoom(int number, int capacity, double basePrice) {
        Room ptr = getRoom(number);
        if (ptr != null) {
            return ptr;
        }
        Room nowy = new Room(number, capacity, basePrice);
        roomRepository.add(nowy);
        return nowy;
    }

    public List<Room> findRooms(Predicate<Room> predicate) {
        return roomRepository.findBy(predicate);
    }

    public List<Room> findAllRooms() {
        return roomRepository.findAll();
    }

    public void generateRooms() {
        registerRoom(1, 2, 220);
        registerRoom(2, 3, 250);
        registerRoom(3, 4, 280);
        registerRoom(4, 5, 310);
        registerRoom(5, 1, 230);
        registerRoom(6, 4, 420);
        registerRoom(7, 2, 320);
        registerRoom(8, 3, 170);
        registerRoom(9, 5, 470);
        registerRoom(10, 3, 310);
        registerRoom(11, 2, 230);
        registerRoom(12, 7, 570);
        registerRoom(13, 6, 470);
        registerRoom(14, 4, 350);
        registerRoom(15, 1, 230);
    }

    public List<Room> getAvailableRooms(LocalDateTime beginDate, LocalDateTime endDate, RentManager rentManager) {
        boolean[] tabBool = new boolean[15];
        for (int i = 0; i < 15; i++) {
            tabBool[i] = true;
        }

        List<Rent> allRents = rentManager.findAllRents();
        for (Rent rent : allRents) {
            if ((endDate.compareTo(rent.getEndDate()) <= 0 && endDate.compareTo(rent.getBeginDate()) > 0) ||
                    (beginDate.compareTo(rent.getEndDate()) < 0 && beginDate.compareTo(rent.getBeginDate()) >= 0) ||
                    (endDate.compareTo(rent.getEndDate()) >= 0 && beginDate.compareTo(rent.getBeginDate()) <= 0)) {
                tabBool[rent.getRoom().getNumber() - 1] = false;
            }
        }

        List<Room> availableRooms = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            if (tabBool[i]) {
                availableRooms.add(getRoom(i + 1));
            }
        }

        return availableRooms;
    }
}