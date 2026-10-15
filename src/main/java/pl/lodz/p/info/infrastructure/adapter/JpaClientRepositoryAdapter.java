package pl.lodz.p.info.infrastructure.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pl.lodz.p.info.infrastructure.entity.*;
import pl.lodz.p.info.model.*;
import pl.lodz.p.info.repositories.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaClientRepositoryAdapter implements ClientRepository {

    @Override
    public Optional<Client> findByPersonalId(String personalId) {
        try (EntityManager em = DatabaseConnection.getEntityManager()) {
            ClientEnt entity = em.createQuery(
                            "SELECT c FROM ClientEnt c WHERE c.personId = :pesel", ClientEnt.class)
                    .setParameter("pesel", new PersonalIdEmbeddable(personalId))
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return Optional.ofNullable(entity).map(this::mapToDomain);
        }
    }

    @Override
    public void save(Client client) {
        EntityManager em = DatabaseConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            ClientEnt existing = em.createQuery(
                            "SELECT c FROM ClientEnt c WHERE c.personId = :pesel", ClientEnt.class)
                    .setParameter("pesel", new PersonalIdEmbeddable(client.getPersonalId()))
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existing == null) {
                ClientEnt entity = mapToEntity(client);
                em.persist(entity);
            } else {
                existing.setFirstName(client.getFirstName());
                existing.setLastName(client.getLastName());
                existing.setArchive(client.isArchive());
                existing.setAddress(new AddressEmbeddable(
                        client.getAddress().getCity(),
                        client.getAddress().getPostcode(),
                        client.getAddress().getStreet(),
                        client.getAddress().getHouseNumber(),
                        client.getAddress().getNumber()
                ));
                existing.setClientType(mapClientTypeToEntity(client));
                em.merge(existing);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Client> findAll() {
        try (EntityManager em = DatabaseConnection.getEntityManager()) {
            List<ClientEnt> entities = em.createQuery("SELECT c FROM ClientEnt c", ClientEnt.class).getResultList();
            return entities.stream().map(this::mapToDomain).collect(Collectors.toList());
        }
    }

    private Client mapToDomain(ClientEnt ent) {
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
                ent.getPersonId().getPersonalId(),
                address,
                ent.getLogin(),
                ent.getPassword()
        );
        client.setArchive(ent.isArchive());
        client.setClientType(mapClientTypeToDomain(ent.getClientType()));
        return client;
    }

    private ClientEnt mapToEntity(Client client) {
        ClientEnt ent = new ClientEnt();
        ent.setFirstName(client.getFirstName());
        ent.setLastName(client.getLastName());
        ent.setPersonId(new PersonalIdEmbeddable(client.getPersonalId()));
        ent.setLogin(client.getLogin());
        ent.setPassword(client.getPassword());
        ent.setArchive(client.isArchive());
        if (client.getAddress() != null) {
            ent.setAddress(new AddressEmbeddable(
                    client.getAddress().getCity(),
                    client.getAddress().getPostcode(),
                    client.getAddress().getStreet(),
                    client.getAddress().getHouseNumber(),
                    client.getAddress().getNumber()
            ));
        }
        ent.setClientType(mapClientTypeToEntity(client));
        return ent;
    }

    private ClientType mapClientTypeToDomain(ClientTypeEnt ent) {
        if (ent instanceof SilverEnt) return new Silver();
        if (ent instanceof GoldEnt) return new Gold();
        if (ent instanceof CrystalEnt) return new Crystal();
        if (ent instanceof EmeraldEnt) return new Emerald();
        return new DefaultClientType();
    }

    private ClientTypeEnt mapClientTypeToEntity(Client client) {
        String typeLetter = client.getLetterRepresentingClientType();
        switch (typeLetter) {
            case "s": return new SilverEnt();
            case "g": return new GoldEnt();
            case "c": return new CrystalEnt();
            case "e": return new EmeraldEnt();
            default: return new DefaultClientTypeEnt();
        }
    }
}