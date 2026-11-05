package pl.lodz.p.info.infrastructure.mongoAdapter;

import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.conversions.Bson;
import pl.lodz.p.info.infrastructure.mongoEntity.RentMongoEnt;
import pl.lodz.p.info.model.Client;
import pl.lodz.p.info.model.Rent;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.ClientRepository;
import pl.lodz.p.info.repositories.RentRepository;
import pl.lodz.p.info.repositories.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoRentRepositoryAdapter implements RentRepository {

    private final MongoCollection<RentMongoEnt> collection;
    private final MongoClient mongoClient;
    private final ClientRepository clientRepository;
    private final RoomRepository roomRepository;

    public MongoRentRepositoryAdapter() {
        this.mongoClient = MongoDatabaseConnection.getMongoClient();
        this.collection = MongoDatabaseConnection.getDatabase().getCollection("rents", RentMongoEnt.class);
        this.clientRepository = new MongoClientRepositoryAdapter();
        this.roomRepository = new MongoRoomRepositoryAdapter();
    }

    @Override
    public Optional<Rent> findById(int id) {
        RentMongoEnt ent = collection.find(Filters.eq("_id", id)).first();
        return Optional.ofNullable(mapToDomain(ent));
    }

    @Override
    public void save(Rent rent) {
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                if (!rent.getIsCanceled()) {
                    Bson query = Filters.and(
                            Filters.eq("roomNumber", rent.getRoom().getNumber()),
                            Filters.eq("isCanceled", false),
                            Filters.lt("beginDate", rent.getEndDate()),
                            Filters.gt("endDate", rent.getBeginDate()),
                            Filters.ne("_id", rent.getId())
                    );
                    RentMongoEnt existing = collection.find(session, query).first();

                    if (existing != null) {
                        throw new IllegalStateException("Naruszenie spójności: Pokój " + rent.getRoom().getNumber() + " jest już wynajęty w tym terminie!");
                    }
                }

                RentMongoEnt ent = mapToMongoEnt(rent);
                collection.replaceOne(session, Filters.eq("_id", rent.getId()), ent, new ReplaceOptions().upsert(true));
                session.commitTransaction();
            } catch (Exception e) {
                session.abortTransaction();
                throw e;
            }
        }
    }

    @Override
    public List<Rent> findAll() {
        List<Rent> rents = new ArrayList<>();
        for (RentMongoEnt ent : collection.find()) {
            Rent r = mapToDomain(ent);
            if (r != null) rents.add(r);
        }
        return rents;
    }

    @Override
    public void delete(Rent rent) {
        collection.deleteOne(Filters.eq("_id", rent.getId()));
    }

    private Rent mapToDomain(RentMongoEnt ent) {
        if (ent == null) return null;

        Client client = clientRepository.findByPersonalId(ent.getClientPersonalId()).orElse(null);
        Room room = roomRepository.findByNumber(ent.getRoomNumber()).orElse(null);

        if (client == null || room == null) return null;

        Rent rent = new Rent(ent.getId(), ent.getBeginDate(), ent.getEndDate(), room, client);
        rent.setIsCanceled(ent.isCanceled());
        if (ent.getRentCost() > 0) {
            rent.endRent();
        }
        return rent;
    }

    private RentMongoEnt mapToMongoEnt(Rent rent) {
        if (rent == null) return null;
        RentMongoEnt ent = new RentMongoEnt();
        ent.setId(rent.getId());
        ent.setBeginDate(rent.getBeginDate());
        ent.setEndDate(rent.getEndDate());
        ent.setRentCost(rent.getRentCost());
        ent.setCanceled(rent.getIsCanceled());
        ent.setClientPersonalId(rent.getClient().getPersonalId());
        ent.setRoomNumber(rent.getRoom().getNumber());
        return ent;
    }
}
