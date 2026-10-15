package pl.lodz.p.info.infrastructure.adapter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.lodz.p.info.infrastructure.adapter.DatabaseConnection;
import pl.lodz.p.info.infrastructure.entity.RoomEnt;
import pl.lodz.p.info.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class JpaRepositoryAdaptersTest {

    private JpaRoomRepositoryAdapter roomAdapter;
    private JpaClientRepositoryAdapter clientAdapter;
    private JpaRentRepositoryAdapter rentAdapter;

    @BeforeEach
    void setUp() {
        roomAdapter = new JpaRoomRepositoryAdapter();
        clientAdapter = new JpaClientRepositoryAdapter();
        rentAdapter = new JpaRentRepositoryAdapter();
    }

    @Test
    @DisplayName("1. CRUD Pokoju - Zapis, odczyt i aktualizacja ")
    void testSaveFindAndUpdateRoom() {
        Room room = new Room(101, 2, 200.0);
        roomAdapter.save(room);

        Optional<Room> found = roomAdapter.findByNumber(101);
        assertTrue(found.isPresent());
        assertEquals(200.0, found.get().getBasePrice());

        Room updatedRoom = new Room(101, 2, 250.0);
        roomAdapter.save(updatedRoom);

        Optional<Room> foundAfterUpdate = roomAdapter.findByNumber(101);
        assertTrue(foundAfterUpdate.isPresent());
        assertEquals(250.0, foundAfterUpdate.get().getBasePrice());
    }

    @Test
    @DisplayName("2. Polimorfizm i mapowanie dziedziczenia ClientType ")
    void testClientTypePolymorphism() {
        Address address = new Address("Łódź", "90-001", "Piotrkowska", "10", "2");

        Client clientSilver = new Client("Jan", "Kowalski", "11111111111", address, "jkowal", "pass");
        clientSilver.setClientType(new Silver());
        clientAdapter.save(clientSilver);

        Client clientEmerald = new Client("Piotr", "Nowak", "22222222222", address, "pnowak", "pass");
        clientEmerald.setClientType(new Emerald());
        clientAdapter.save(clientEmerald);

        Optional<Client> fetchedSilver = clientAdapter.findByPersonalId("11111111111");
        Optional<Client> fetchedEmerald = clientAdapter.findByPersonalId("22222222222");

        assertTrue(fetchedSilver.isPresent());
        assertEquals("s", fetchedSilver.get().getLetterRepresentingClientType());

        assertTrue(fetchedEmerald.isPresent());
        assertEquals("e", fetchedEmerald.get().getLetterRepresentingClientType());
    }

    @Test
    @DisplayName("3. Relacja do wielu ")
    void testOneToManyClientToRents() {
        Address address = new Address("Warszawa", "00-001", "Prosta", "1", "");
        Client client = new Client("Adam", "Cichy", "33333333333", address, "acichy", "pass");
        Room room1 = new Room(201, 1, 100.0);
        Room room2 = new Room(202, 2, 200.0);

        clientAdapter.save(client);
        roomAdapter.save(room1);
        roomAdapter.save(room2);

        Rent rent1 = new Rent(1, LocalDateTime.now(), LocalDateTime.now().plusDays(2), room1, client);
        Rent rent2 = new Rent(2, LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(5), room2, client);

        rentAdapter.save(rent1);
        rentAdapter.save(rent2);

        List<Rent> allRents = rentAdapter.findAll();
        long clientRentsCount = allRents.stream()
                .filter(r -> r.getClient().getPersonalId().equals("33333333333"))
                .count();

        assertEquals(2, clientRentsCount);
    }

    @Test
    @DisplayName("4. Usunięcie / Anulowanie wypożyczenia ")
    void testDeleteRent() {
        Address address = new Address("Gdańsk", "80-001", "Długa", "5", "");
        Client client = new Client("Maria", "Zelent", "44444444444", address, "mzelent", "pass");
        Room room = new Room(301, 2, 150.0);

        clientAdapter.save(client);
        roomAdapter.save(room);

        Rent rent = new Rent(99, LocalDateTime.now(), LocalDateTime.now().plusDays(1), room, client);
        rentAdapter.save(rent);

        rentAdapter.delete(rent);

        Optional<Rent> deletedRent = rentAdapter.findById(99);
        assertTrue(deletedRent.isEmpty());
    }

    @Test
    @DisplayName("5. Zachowanie reguł ACID ")
    void testAcidTransactionRollback() {
        int initialRoomCount = roomAdapter.findAll().size();

        EntityManager em = DatabaseConnection.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();
        try {

            RoomEnt roomEnt = new RoomEnt();
            roomEnt.setNumber(999);
            roomEnt.setCapacity(2);
            roomEnt.setBasePrice(300.0);
            em.persist(roomEnt);


            throw new RuntimeException("Błąddd");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            em.close();
        }

        int finalRoomCount = roomAdapter.findAll().size();
        assertEquals(initialRoomCount, finalRoomCount);
        assertTrue(roomAdapter.findByNumber(999).isEmpty());
    }
}