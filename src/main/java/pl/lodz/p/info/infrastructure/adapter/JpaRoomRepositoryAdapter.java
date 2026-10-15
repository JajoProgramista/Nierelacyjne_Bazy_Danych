package pl.lodz.p.info.infrastructure.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import pl.lodz.p.info.infrastructure.entity.RoomEnt;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.RoomRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaRoomRepositoryAdapter implements RoomRepository {

    @Override
    public Optional<Room> findByNumber(int number) {
        try (EntityManager em = DatabaseConnection.getEntityManager()) {
            RoomEnt entity = em.createQuery(
                            "SELECT r FROM RoomEnt r WHERE r.number = :num", RoomEnt.class)
                    .setParameter("num", number)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            return Optional.ofNullable(entity).map(this::mapToDomain);
        }
    }

    @Override
    public void save(Room room) {
        EntityManager em = DatabaseConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            RoomEnt existing = em.createQuery(
                            "SELECT r FROM RoomEnt r WHERE r.number = :num", RoomEnt.class)
                    .setParameter("num", room.getNumber())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existing == null) {
                RoomEnt entity = mapToEntity(room);
                em.persist(entity);
            } else {
                existing.setCapacity(room.getCapacity());
                existing.setBasePrice(room.getBasePrice());
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
    public List<Room> findAll() {
        try (EntityManager em = DatabaseConnection.getEntityManager()) {
            List<RoomEnt> entities = em.createQuery("SELECT r FROM RoomEnt r", RoomEnt.class).getResultList();
            return entities.stream().map(this::mapToDomain).collect(Collectors.toList());
        }
    }


    private Room mapToDomain(RoomEnt ent) {
        if (ent == null) return null;
        return new Room(ent.getNumber(), ent.getCapacity(), ent.getBasePrice());
    }

    private RoomEnt mapToEntity(Room room) {
        if (room == null) return null;
        RoomEnt ent = new RoomEnt();
        ent.setNumber(room.getNumber());
        ent.setCapacity(room.getCapacity());
        ent.setBasePrice(room.getBasePrice());
        return ent;
    }
}