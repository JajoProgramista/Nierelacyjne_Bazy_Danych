package pl.lodz.p.info.infrastructure.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.RollbackException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lodz.p.info.infrastructure.adapter.DatabaseConnection;
import pl.lodz.p.info.infrastructure.entity.RoomEnt;
import pl.lodz.p.info.model.Room;

import static org.junit.jupiter.api.Assertions.*;

class OptimisticLockingTest {

    @Test
    @DisplayName("Współbieżne wypożyczenie pokoju - Weryfikacja blokady optymistycznej (OPTIMISTIC_FORCE_INCREMENT)")
    void testConcurrentRoomRentalOptimisticLock() {
        JpaRoomRepositoryAdapter roomAdapter = new JpaRoomRepositoryAdapter();
        Room room = new Room(505, 2, 500.0);
        roomAdapter.save(room);

        EntityManager emSetup = DatabaseConnection.getEntityManager();
        Long roomId = emSetup.createQuery("SELECT r.id FROM RoomEnt r WHERE r.number = 505", Long.class)
                .getSingleResult();
        emSetup.close();

        EntityManager emUser1 = DatabaseConnection.getEntityManager();
        EntityManager emUser2 = DatabaseConnection.getEntityManager();

        EntityTransaction tx1 = emUser1.getTransaction();
        EntityTransaction tx2 = emUser2.getTransaction();

        tx1.begin();
        tx2.begin();

        RoomEnt roomForUser1 = emUser1.find(RoomEnt.class, roomId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
        RoomEnt roomForUser2 = emUser2.find(RoomEnt.class, roomId, LockModeType.OPTIMISTIC_FORCE_INCREMENT);

        assertNotNull(roomForUser1);
        assertNotNull(roomForUser2);

        tx1.commit();
        emUser1.close();

        RollbackException rollbackException = assertThrows(RollbackException.class, tx2::commit);

        Throwable cause = rollbackException.getCause();
        assertNotNull(cause, "Oczekiwano przyczyny wycofania transakcji");

        boolean isConcurrencyConflict = cause instanceof OptimisticLockException
                || cause instanceof org.hibernate.StaleObjectStateException
                || cause instanceof org.hibernate.exception.LockAcquisitionException
                || (cause.getMessage() != null && cause.getMessage().contains("concurrent update"));

        assertTrue(isConcurrencyConflict,
                "Oczekiwano błędu blokady optymistycznej lub konfliktu współbieżności, a był: " + cause);

        emUser2.close();
    }
}