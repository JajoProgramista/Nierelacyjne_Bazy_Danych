package pl.lodz.p.info.infrastructure.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.lodz.p.info.infrastructure.redisAdapter.RedisRoomRepositoryDecorator;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.RoomRepository;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RedisRoomRepositoryDecoratorTest {

    private RoomRepository mongoRepositoryMock;
    private JedisPooled jedisMock;
    private RedisRoomRepositoryDecorator decorator;

    @BeforeEach
    void setUp() {
        mongoRepositoryMock = Mockito.mock(RoomRepository.class);
        jedisMock = Mockito.mock(JedisPooled.class);
        decorator = new RedisRoomRepositoryDecorator(mongoRepositoryMock, jedisMock);
    }

    @Test
    void testCacheRoom_ShouldSerializeAndSaveWithTtl() {
        Room room = new Room(101, 2, 250.0);

        decorator.cacheRoom(room);

        verify(jedisMock, times(1)).jsonSet(eq("room:101"), contains("\"number\":101"));
        verify(jedisMock, times(1)).expire("room:101", 3600L);
    }

    @Test
    void testGetRoomFromCache_WhenPresent_ShouldReturnDeserializedRoom() {
        String json = "{\"number\":101,\"capacity\":2,\"basePrice\":250.0,\"isRented\":0}";
        when(jedisMock.jsonGet("room:101")).thenReturn(json);

        Optional<Room> result = decorator.getRoomFromCache(101);

        assertTrue(result.isPresent());
        assertEquals(101, result.get().getNumber());
        assertEquals(2, result.get().getCapacity());
        assertEquals(250.0, result.get().getBasePrice());
    }

    @Test
    void testFindByNumber_CacheHit_ShouldNotQueryMongo() {
        String json = "{\"number\":101,\"capacity\":2,\"basePrice\":250.0,\"isRented\":0}";
        when(jedisMock.jsonGet("room:101")).thenReturn(json);

        Optional<Room> result = decorator.findByNumber(101);

        assertTrue(result.isPresent());
        assertEquals(101, result.get().getNumber());
        verify(mongoRepositoryMock, never()).findByNumber(anyInt());
    }

    @Test
    void testFindByNumber_CacheMiss_ShouldQueryMongoAndSaveToCache() {
        when(jedisMock.jsonGet("room:102")).thenReturn(null);
        Room dbRoom = new Room(102, 3, 300.0);
        when(mongoRepositoryMock.findByNumber(102)).thenReturn(Optional.of(dbRoom));

        Optional<Room> result = decorator.findByNumber(102);

        assertTrue(result.isPresent());
        assertEquals(102, result.get().getNumber());
        verify(mongoRepositoryMock, times(1)).findByNumber(102);
        verify(jedisMock, times(1)).jsonSet(eq("room:102"), anyString());
        verify(jedisMock, times(1)).expire("room:102", 3600L);
    }

    @Test
    void testSave_ShouldPersistInMongoAndInvalidateCache() {
        Room room = new Room(101, 2, 290.0);

        decorator.save(room);

        verify(mongoRepositoryMock, times(1)).save(room);
        verify(jedisMock, times(1)).jsonDel("room:101");
        verify(jedisMock, times(1)).jsonDel("rooms:all");
    }

    @Test
    void testFailover_WhenRedisThrowsException_ShouldFallbackToMongo() {
        when(jedisMock.jsonGet("room:105")).thenThrow(new JedisConnectionException("Connection refused"));
        Room dbRoom = new Room(105, 4, 420.0);
        when(mongoRepositoryMock.findByNumber(105)).thenReturn(Optional.of(dbRoom));

        Optional<Room> result = decorator.findByNumber(105);

        assertTrue(result.isPresent());
        assertEquals(105, result.get().getNumber());
        verify(mongoRepositoryMock, times(1)).findByNumber(105);
    }

    @Test
    void testFindAll_Failover_WhenRedisIsDown_ShouldFallbackToMongo() {
        when(jedisMock.jsonGet("rooms:all")).thenThrow(new JedisConnectionException("Redis down"));
        when(mongoRepositoryMock.findAll()).thenReturn(List.of(new Room(1, 2, 200.0)));

        List<Room> rooms = decorator.findAll();

        assertEquals(1, rooms.size());
        verify(mongoRepositoryMock, times(1)).findAll();
    }
}
