package pl.lodz.p.info.infrastructure.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pl.lodz.p.info.infrastructure.entity.ClientEnt;
import pl.lodz.p.info.infrastructure.entity.PersonalIdEmbeddable;
import pl.lodz.p.info.infrastructure.entity.RentEnt;
import pl.lodz.p.info.infrastructure.entity.RoomEnt;
import pl.lodz.p.info.model.Address;
import pl.lodz.p.info.model.Client;
import pl.lodz.p.info.model.Rent;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.RentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaRentRepositoryAdapter implements RentRepository {

    @Override
    public Optional<Rent> findById(int id) {
        try (EntityManager em = DatabaseConnection.getEntityManager()) {
            RentEnt entity = em.createQuery(
                            "SELECT r FROM RentEnt r JOIN FETCH r.client JOIN FETCH r.room WHERE r.businessId = :bid", RentEnt.class)
                    .setParameter("bid", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return Optional.ofNullable(entity).map(this::mapToDomain);
        }
    }

    @Override
    public void save(Rent rent) {
        EntityManager em = DatabaseConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            RentEnt existing = em.createQuery(
                            "SELECT r FROM RentEnt r WHERE r.businessId = :bid", RentEnt.class)
                    .setParameter("bid", rent.getId())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            ClientEnt clientEnt = em.createQuery(
                            "SELECT c FROM ClientEnt c WHERE c.personId = :pesel", ClientEnt.class)
                    .setParameter("pesel", new PersonalIdEmbeddable(rent.getClient().getPersonalId()))
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono klienta o PESEL: " + rent.getClient().getPersonalId()));

            RoomEnt roomEnt = em.createQuery(
                            "SELECT r FROM RoomEnt r WHERE r.number = :num", RoomEnt.class)
                    .setParameter("num", rent.getRoom().getNumber())
                    .getResultStream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono pokoju o numerze: " + rent.getRoom().getNumber()));

            if (existing == null) {
                RentEnt newEnt = new RentEnt();
                newEnt.setBusinessId(rent.getId());
                newEnt.setBeginDate(rent.getBeginDate());
                newEnt.setEndDate(rent.getEndDate());
                newEnt.setRentCost(rent.getRentCost());
                newEnt.setCanceled(rent.getIsCanceled());
                newEnt.setClient(clientEnt);
                newEnt.setRoom(roomEnt);
                em.persist(newEnt);
            } else {
                existing.setBeginDate(rent.getBeginDate());
                existing.setEndDate(rent.getEndDate());
                existing.setRentCost(rent.getRentCost());
                existing.setCanceled(rent.getIsCanceled());
                existing.setClient(clientEnt);
                existing.setRoom(roomEnt);
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
    public List<Rent> findAll() {
        try (EntityManager em = DatabaseConnection.getEntityManager()) {
            List<RentEnt> entities = em.createQuery(
                    "SELECT r FROM RentEnt r JOIN FETCH r.client JOIN FETCH r.room", RentEnt.class).getResultList();
            return entities.stream().map(this::mapToDomain).collect(Collectors.toList());
        }
    }

    @Override
    public void delete(Rent rent) {
        EntityManager em = DatabaseConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            RentEnt existing = em.createQuery(
                            "SELECT r FROM RentEnt r WHERE r.businessId = :bid", RentEnt.class)
                    .setParameter("bid", rent.getId())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existing != null) {
                em.remove(existing);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }


    private Rent mapToDomain(RentEnt ent) {
        if (ent == null) return null;

        Client client = mapClientToDomain(ent.getClient());
        Room room = mapRoomToDomain(ent.getRoom());

        Rent rent = new Rent(
                ent.getBusinessId(),
                ent.getBeginDate(),
                ent.getEndDate(),
                room,
                client
        );
        rent.setIsCanceled(ent.isCanceled());
        return rent;
    }

    private Room mapRoomToDomain(RoomEnt roomEnt) {
        if (roomEnt == null) return null;
        return new Room(roomEnt.getNumber(), roomEnt.getCapacity(), roomEnt.getBasePrice());
    }

    private Client mapClientToDomain(ClientEnt clientEnt) {
        if (clientEnt == null) return null;
        Address address = new Address(
                clientEnt.getAddress().getCity(),
                clientEnt.getAddress().getPostcode(),
                clientEnt.getAddress().getStreet(),
                clientEnt.getAddress().getHouseNumber(),
                clientEnt.getAddress().getApartmentNumber()
        );
        Client client = new Client(
                clientEnt.getFirstName(),
                clientEnt.getLastName(),
                clientEnt.getPersonId().getPersonalId(),
                address,
                clientEnt.getLogin(),
                clientEnt.getPassword()
        );
        client.setArchive(clientEnt.isArchive());
        return client;
    }
}