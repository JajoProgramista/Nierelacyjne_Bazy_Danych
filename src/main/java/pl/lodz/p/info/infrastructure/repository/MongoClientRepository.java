package pl.lodz.p.info.infrastructure.repository;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoDatabaseConnection;
import pl.lodz.p.info.infrastructure.mongoEntity.ClientMongoEnt;

import java.util.ArrayList;
import java.util.List;

public class MongoClientRepository {

    private final MongoCollection<ClientMongoEnt> collection;

    public MongoClientRepository() {
        this.collection = MongoDatabaseConnection.getDatabase().getCollection("clients", ClientMongoEnt.class);
    }

    public ClientMongoEnt findByPersonalId(String personalId) {
        return collection.find(Filters.eq("_id", personalId)).first();
    }

    public void save(ClientMongoEnt clientMongoEnt) {
        collection.replaceOne(
                Filters.eq("_id", clientMongoEnt.getPersonalId()),
                clientMongoEnt,
                new ReplaceOptions().upsert(true)
        );
    }

    public List<ClientMongoEnt> findAll() {
        List<ClientMongoEnt> clients = new ArrayList<>();
        for (ClientMongoEnt ent : collection.find()) {
            clients.add(ent);
        }
        return clients;
    }
}
