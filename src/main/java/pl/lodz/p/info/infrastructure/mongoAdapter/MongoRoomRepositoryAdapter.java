package pl.lodz.p.info.infrastructure.mongoAdapter;

import pl.lodz.p.info.infrastructure.mongoEntity.RoomMongoEnt;
import pl.lodz.p.info.infrastructure.repository.MongoRoomRepository;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoRoomRepositoryAdapter implements RoomRepository {

    private final MongoRoomRepository repository;

    public MongoRoomRepositoryAdapter() {
        this.repository = new MongoRoomRepository();
    }

    @Override
    public Optional<Room> findByNumber(int number) {
        RoomMongoEnt ent = repository.findByNumber(number);
        return Optional.ofNullable(mapToDomain(ent));
    }

    @Override
    public void save(Room room) {
        RoomMongoEnt ent = mapToMongoEnt(room);
        repository.save(ent);
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        for (RoomMongoEnt ent : repository.findAll()) {
            rooms.add(mapToDomain(ent));
        }
        return rooms;
    }

    private Room mapToDomain(RoomMongoEnt ent) {
        if (ent == null) return null;
        return new Room(ent.getNumber(), ent.getCapacity(), ent.getBasePrice());
    }

    private RoomMongoEnt mapToMongoEnt(Room room) {
        if (room == null) return null;
        RoomMongoEnt ent = new RoomMongoEnt();
        ent.setNumber(room.getNumber());
        ent.setCapacity(room.getCapacity());
        ent.setBasePrice(room.getBasePrice());
        return ent;
    }
}