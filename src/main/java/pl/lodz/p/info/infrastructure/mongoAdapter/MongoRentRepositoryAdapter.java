package pl.lodz.p.info.infrastructure.mongoAdapter;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import pl.lodz.p.info.infrastructure.mongoEntity.RentMongoEnt;
import pl.lodz.p.info.infrastructure.repository.MongoRentRepository;
import pl.lodz.p.info.model.*;
import pl.lodz.p.info.repositories.RentRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MongoRentRepositoryAdapter implements RentRepository {

    private final MongoRentRepository repository;
    private final MongoClient mongoClient;

    public MongoRentRepositoryAdapter() {
        this.repository = new MongoRentRepository();
        this.mongoClient = MongoDatabaseConnection.getMongoClient();
    }

    @Override
    public Optional<Rent> findById(int id) {
        Document doc = repository.findByIdWithDetails(id);
        return Optional.ofNullable(mapToDomainFromDocument(doc));
    }

    @Override
    public void save(Rent rent) {
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            try {
                RentMongoEnt ent = mapToMongoEnt(rent);
                repository.save(session, ent);


                if (!rent.getIsCanceled() && rent.getRentCost() == 0.0) {

                    repository.incrementRoomRentStatus(session, rent.getRoom().getNumber(), 1);
                }

                else if (rent.getRentCost() > 0.0) {
                    repository.incrementRoomRentStatus(session, rent.getRoom().getNumber(), -1);
                }

                session.commitTransaction();
            } catch (MongoWriteException | MongoCommandException e) {
                session.abortTransaction();
                throw new IllegalStateException("Naruszenie spójności: Pokój " + rent.getRoom().getNumber() + " jest już wynajęty!", e);
            } catch (Exception e) {
                session.abortTransaction();
                throw e;
            }
        }
    }

    @Override
    public List<Rent> findAll() {
        List<Rent> rents = new ArrayList<>();
        for (Document doc : repository.findAllWithDetails()) {
            Rent r = mapToDomainFromDocument(doc);
            if (r != null) rents.add(r);
        }
        return rents;
    }

    @Override
    public void delete(Rent rent) {
        repository.delete(rent.getId());
    }

    private Rent mapToDomainFromDocument(Document doc) {
        if (doc == null) return null;

        Document roomDoc = doc.get("roomObj", Document.class);
        Room room = new Room(
                roomDoc.getInteger("_id"),
                roomDoc.getInteger("capacity"),
                roomDoc.getDouble("basePrice")
        );

        Document clientDoc = doc.get("clientObj", Document.class);
        Document addressDoc = clientDoc.get("address", Document.class);
        Address address = new Address(
                addressDoc.getString("city"),
                addressDoc.getString("postcode"),
                addressDoc.getString("street"),
                addressDoc.getString("houseNumber"),
                addressDoc.getString("apartmentNumber")
        );

        Client client = new Client(
                clientDoc.getString("firstName"),
                clientDoc.getString("lastName"),
                clientDoc.getString("_id"),
                address,
                clientDoc.getString("login"),
                clientDoc.getString("password")
        );
        client.setArchive(clientDoc.getBoolean("archive", false));

        Document typeDoc = clientDoc.get("clientType", Document.class);
        String clazz = typeDoc != null ? typeDoc.getString("_clazz") : "default";
        switch (clazz) {
            case "silver" -> client.setClientType(new Silver());
            case "gold" -> client.setClientType(new Gold());
            case "crystal" -> client.setClientType(new Crystal());
            case "emerald" -> client.setClientType(new Emerald());
            default -> client.setClientType(new DefaultClientType());
        }

        Rent rent = new Rent(
                doc.getInteger("_id"),
                convertToLocalDateTime(doc.getDate("beginDate")),
                convertToLocalDateTime(doc.getDate("endDate")),
                room,
                client
        );
        rent.setIsCanceled(doc.getBoolean("isCanceled", false));
        if (doc.getDouble("rentCost") > 0) {
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

    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}