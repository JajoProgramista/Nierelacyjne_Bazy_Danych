package pl.lodz.p.info.infrastructure.mongoAdapter;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import pl.lodz.p.info.infrastructure.mongoEntity.RoomMongoEnt;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoRoomRepositoryAdapter implements RoomRepository {

    private final MongoCollection<RoomMongoEnt> collection;

    public MongoRoomRepositoryAdapter() {
        this.collection = MongoDatabaseConnection.getDatabase().getCollection("rooms", RoomMongoEnt.class);
    }

    @Override
    public Optional<Room> findByNumber(int number) {
        RoomMongoEnt ent = collection.find(Filters.eq("_id", number)).first();
        return Optional.ofNullable(mapToDomain(ent));
    }

    @Override
    public void save(Room room) {
        RoomMongoEnt ent = mapToMongoEnt(room);
        collection.replaceOne(Filters.eq("_id", room.getNumber()), ent, new ReplaceOptions().upsert(true));
    }

    @Override
    public List<Room> findAll() {
        List<Room> rooms = new ArrayList<>();
        for (RoomMongoEnt ent : collection.find()) {
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
