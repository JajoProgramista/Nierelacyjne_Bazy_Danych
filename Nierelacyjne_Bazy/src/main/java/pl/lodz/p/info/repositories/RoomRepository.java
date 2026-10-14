package pl.lodz.p.info.repositories;

import pl.lodz.p.info.model.Room;
import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Optional<Room> findByNumber(int number);
    void save(Room room);
    List<Room> findAll();
}