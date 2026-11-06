package pl.lodz.p.info.infrastructure.repository;

import com.mongodb.client.model.Updates;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoDatabaseConnection;
import pl.lodz.p.info.infrastructure.mongoEntity.RentMongoEnt;


import java.util.ArrayList;
import java.util.List;

public class MongoRentRepository {

    private final MongoCollection<RentMongoEnt> collection;
    private final MongoCollection<Document> docCollection;

    public MongoRentRepository() {
        this.collection = MongoDatabaseConnection.getDatabase().getCollection("rents", RentMongoEnt.class);
        this.docCollection = MongoDatabaseConnection.getDatabase().getCollection("rents");
    }

    public void save(ClientSession session, RentMongoEnt ent) {
        collection.replaceOne(session, Filters.eq("_id", ent.getId()), ent, new ReplaceOptions().upsert(true));
    }

    public void incrementRoomRentStatus(ClientSession session, int roomNumber, int incrementValue) {
        MongoCollection<Document> roomsCollection = MongoDatabaseConnection.getDatabase().getCollection("rooms");
        roomsCollection.updateOne(
                session,
                Filters.eq("_id", roomNumber),
                Updates.inc("isRented", incrementValue)
        );
    }

    public void delete(int id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    public Document findByIdWithDetails(int id) {
        List<Bson> pipeline = List.of(
                Aggregates.match(Filters.eq("_id", id)),
                Aggregates.lookup("clients", "clientPersonalId", "_id", "clientObj"),
                Aggregates.unwind("$clientObj"),
                Aggregates.lookup("rooms", "roomNumber", "_id", "roomObj"),
                Aggregates.unwind("$roomObj")
        );
        return docCollection.aggregate(pipeline).first();
    }

    public List<Document> findAllWithDetails() {
        List<Bson> pipeline = List.of(
                Aggregates.lookup("clients", "clientPersonalId", "_id", "clientObj"),
                Aggregates.unwind("$clientObj"),
                Aggregates.lookup("rooms", "roomNumber", "_id", "roomObj"),
                Aggregates.unwind("$roomObj")
        );
        return docCollection.aggregate(pipeline).into(new ArrayList<>());
    }

}
