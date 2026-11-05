package pl.lodz.p.info.infrastructure.mongoAdapter;

import pl.lodz.p.info.infrastructure.mongoEntity.*;
import pl.lodz.p.info.infrastructure.repository.MongoClientRepository;
import pl.lodz.p.info.model.*;
import pl.lodz.p.info.repositories.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoClientRepositoryAdapter implements ClientRepository {

    private final MongoClientRepository repository;

    public MongoClientRepositoryAdapter() {
        this.repository = new MongoClientRepository();
    }

    @Override
    public Optional<Client> findByPersonalId(String personalId) {
        ClientMongoEnt ent = repository.findByPersonalId(personalId);
        return Optional.ofNullable(mapToDomain(ent));
    }

    @Override
    public void save(Client client) {
        ClientMongoEnt ent = mapToMongoEnt(client);
        repository.save(ent);
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        for (ClientMongoEnt ent : repository.findAll()) {
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

        ClientTypeMongoEnt typeEnt = ent.getClientType();
        if (typeEnt instanceof SilverMongoEnt) {
            client.setClientType(new Silver());
        } else if (typeEnt instanceof GoldMongoEnt) {
            client.setClientType(new Gold());
        } else if (typeEnt instanceof CrystalMongoEnt) {
            client.setClientType(new Crystal());
        } else if (typeEnt instanceof EmeraldMongoEnt) {
            client.setClientType(new Emerald());
        } else {
            client.setClientType(new DefaultClientType());
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

        ClientTypeMongoEnt typeEnt = switch (client.getLetterRepresentingClientType()) {
            case "s" -> new SilverMongoEnt();
            case "g" -> new GoldMongoEnt();
            case "c" -> new CrystalMongoEnt();
            case "e" -> new EmeraldMongoEnt();
            default -> new DefaultClientTypeMongoEnt();
        };
        ent.setClientType(typeEnt);

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