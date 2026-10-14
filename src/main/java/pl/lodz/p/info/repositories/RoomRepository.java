package pl.lodz.p.info.repositories;

import pl.lodz.p.info.model.Room;
import java.util.List;

public class RoomRepository extends Repository<Room> {
    public Room findByNumber(int number) {
        List<Room> tmp = findBy(room -> room.getNumber() == number);
        return tmp.isEmpty() ? null : tmp.get(0);
    }
}