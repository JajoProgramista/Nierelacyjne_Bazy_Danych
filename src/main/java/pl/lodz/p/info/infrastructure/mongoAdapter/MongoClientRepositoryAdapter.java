package pl.lodz.p.info.infrastructure.mongoAdapter;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import pl.lodz.p.info.infrastructure.mongoEntity.AddressMongoEnt;
import pl.lodz.p.info.infrastructure.mongoEntity.ClientMongoEnt;
import pl.lodz.p.info.model.*;
import pl.lodz.p.info.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoClientRepositoryAdapter implements ClientRepository {

    private final MongoCollection<ClientMongoEnt> collection;

    public MongoClientRepositoryAdapter() {
        this.collection = MongoDatabaseConnection.getDatabase().getCollection("clients", ClientMongoEnt.class);
    }

    @Override
    public Optional<Client> findByPersonalId(String personalId) {
        ClientMongoEnt ent = collection.find(Filters.eq("_id", personalId)).first();
        return Optional.ofNullable(mapToDomain(ent));
    }

    @Override
    public void save(Client client) {
        ClientMongoEnt ent = mapToMongoEnt(client);
        collection.replaceOne(Filters.eq("_id", client.getPersonalId()), ent, new ReplaceOptions().upsert(true));
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        for (ClientMongoEnt ent : collection.find()) {
            clients.add(mapToDomain(ent));
        }
        return clients;
    }

    private Client mapToDomain(ClientMongoEnt ent) {
        if (ent == null) return null;

        Address address = new Address(
                ent.getAddress().getCity(),
                ent.getAddress().getPostcode(),
                ent.getAddress().getStreet(),
                ent.getAddress().getHouseNumber(),
                ent.getAddress().getApartmentNumber()
        );

        Client client = new Client(
                ent.getFirstName(),
                ent.getLastName(),
                ent.getPersonalId(),
                address,
                ent.getLogin(),
                ent.getPassword()
        );
        client.setArchive(ent.isArchive());

        switch (ent.getClientTypeLetter()) {
            case "s": client.setClientType(new Silver()); break;
            case "g": client.setClientType(new Gold()); break;
            case "c": client.setClientType(new Crystal()); break;
            case "e": client.setClientType(new Emerald()); break;
            default: client.setClientType(new DefaultClientType()); break;
        }

        return client;
    }

    private ClientMongoEnt mapToMongoEnt(Client client) {
        if (client == null) return null;

        ClientMongoEnt ent = new ClientMongoEnt();
        ent.setPersonalId(client.getPersonalId());
        ent.setFirstName(client.getFirstName());
        ent.setLastName(client.getLastName());
        ent.setLogin(client.getLogin());
        ent.setPassword(client.getPassword());
        ent.setArchive(client.isArchive());
        ent.setClientTypeLetter(client.getLetterRepresentingClientType());

        if (client.getAddress() != null) {
            AddressMongoEnt addrEnt = new AddressMongoEnt(
                    client.getAddress().getCity(),
                    client.getAddress().getPostcode(),
                    client.getAddress().getStreet(),
                    client.getAddress().getHouseNumber(),
                    client.getAddress().getNumber()
            );
            ent.setAddress(addrEnt);
        }
        return ent;
    }
}
