package pl.lodz.p.info.infrastructure.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoDatabaseConnection;
import pl.lodz.p.info.infrastructure.mongoEntity.RoomMongoEnt;

import java.util.ArrayList;
import java.util.List;

public class MongoRoomRepository {

    private final MongoCollection<RoomMongoEnt> collection;

    public MongoRoomRepository() {
        this.collection = MongoDatabaseConnection.getDatabase().getCollection("rooms", RoomMongoEnt.class);
    }

    public RoomMongoEnt findByNumber(int number) {
        return collection.find(Filters.eq("_id", number)).first();
    }

    public void save(RoomMongoEnt roomMongoEnt) {
        collection.replaceOne(
                Filters.eq("_id", roomMongoEnt.getNumber()),
                roomMongoEnt,
                new ReplaceOptions().upsert(true)
        );
    }

    public List<RoomMongoEnt> findAll() {
        List<RoomMongoEnt> rooms = new ArrayList<>();
        for (RoomMongoEnt ent : collection.find()) {
            rooms.add(ent);
        }
        return rooms;
    }
}
