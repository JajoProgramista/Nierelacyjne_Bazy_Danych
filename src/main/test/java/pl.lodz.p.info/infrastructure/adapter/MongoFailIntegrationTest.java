package pl.lodz.p.info.infrastructure.adapter;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoClientRepositoryAdapter;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoDatabaseConnection;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoRoomRepositoryAdapter;
import pl.lodz.p.info.model.*;
import pl.lodz.p.info.repositories.ClientRepository;
import pl.lodz.p.info.repositories.RoomRepository;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MongoFailIntegrationTest {

    static GenericContainer<?> mongo1 = new GenericContainer<>(DockerImageName.parse("mongo:6.0"))
            .withCommand("--replSet", "rs0", "--bind_ip_all", "--port", "27017")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort());

    static GenericContainer<?> mongo2 = new GenericContainer<>(DockerImageName.parse("mongo:6.0"))
            .withCommand("--replSet", "rs0", "--bind_ip_all", "--port", "27017")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort());

    static GenericContainer<?> mongo3 = new GenericContainer<>(DockerImageName.parse("mongo:6.0"))
            .withCommand("--replSet", "rs0", "--bind_ip_all", "--port", "27017")
            .withExposedPorts(27017)
            .waitingFor(Wait.forListeningPort());

    @BeforeAll
    static void setUpCluster() {
        mongo1.start();
        mongo2.start();
        mongo3.start();

        String connectionStringUri = String.format(
                "mongodb://%s:%d,%s:%d,%s:%d/?replicaSet=rs0",
                mongo1.getHost(), mongo1.getMappedPort(27017),
                mongo2.getHost(), mongo2.getMappedPort(27017),
                mongo3.getHost(), mongo3.getMappedPort(27017)
        );

        System.setProperty("MONGO_URI", connectionStringUri);

        try {
            String initCommand = "rs.initiate({_id: 'rs0', members: [" +
                    "{_id: 0, host: '" + mongo1.getHost() + ":" + mongo1.getMappedPort(27017) + "'}," +
                    "{_id: 1, host: '" + mongo2.getHost() + ":" + mongo2.getMappedPort(27017) + "'}," +
                    "{_id: 2, host: '" + mongo3.getHost() + ":" + mongo3.getMappedPort(27017) + "'}" +
                    "]})";

            mongo1.execInContainer("mongosh", "--eval", initCommand);
            Thread.sleep(5000);
        } catch (Exception e) {
            throw new RuntimeException("Nie udało się zainicjować klastra testowego", e);
        }
    }

    @AfterAll
    static void tearDownCluster() {
        MongoDatabaseConnection.close();
        mongo1.stop();
        mongo2.stop();
        mongo3.stop();
    }

    @Test
    @Order(1)
    void testStandardClientCrudAndInheritanceCodecs() {
        ClientRepository clientRepository = new MongoClientRepositoryAdapter();

        Address address = new Address("Lodz", "90-001", "Piotrkowska", "10", "5");
        Client client = new Client("Jan", "Kowalski", "12345678901", address, "jankow", "pass123");
        client.setClientType(new Gold());

        clientRepository.save(client);

        Optional<Client> found = clientRepository.findByPersonalId("12345678901");
        assertTrue(found.isPresent());
        assertEquals("Jan", found.get().getFirstName());
        assertInstanceOf(Gold.class, found.get().getClientType());
    }

    @Test
    @Order(2)
    void testClusterFailoverPrimaryShutdown() throws InterruptedException {
        RoomRepository roomRepository = new MongoRoomRepositoryAdapter();

        Room room = new Room(101, 2, 150.0);
        roomRepository.save(room);

        Optional<Room> roomBefore = roomRepository.findByNumber(101);
        assertTrue(roomBefore.isPresent());

        System.out.println(">>> WYŁĄCZAMY WĘZEŁ PRIMARY");
        mongo1.stop();

        Thread.sleep(6000);


        Room roomAfterFailover = new Room(102, 4, 300.0);
        roomRepository.save(roomAfterFailover);

        Optional<Room> foundAfter = roomRepository.findByNumber(102);
        assertTrue(foundAfter.isPresent(), "Aplikacja powinna zapisać dane po awarii węzła Primary dzięki Replica Set!");
        assertEquals(300.0, foundAfter.get().getBasePrice());

        System.out.println("Sukces!!!");
    }
}
