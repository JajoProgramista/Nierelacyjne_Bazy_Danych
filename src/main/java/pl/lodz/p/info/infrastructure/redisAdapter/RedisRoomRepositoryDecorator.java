package pl.lodz.p.info.infrastructure.redisAdapter;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.repositories.RoomRepository;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RedisRoomRepositoryDecorator implements RoomRepository {

    private static final String ROOM_KEY_PREFIX = "room:";
    private static final String ALL_ROOMS_KEY = "rooms:all";
    private static final long ROOM_TTL_SECONDS = 3600L;
    private static final long ALL_ROOMS_TTL_SECONDS = 300L;

    private final RoomRepository decoratedRepository;
    private final JedisPooled jedis;
    private final Jsonb jsonb;

    public RedisRoomRepositoryDecorator(RoomRepository decoratedRepository) {
        this(decoratedRepository, RedisConnection.getJedisPooled());
    }

    public RedisRoomRepositoryDecorator(RoomRepository decoratedRepository, JedisPooled jedis) {
        this.decoratedRepository = decoratedRepository;
        this.jedis = jedis;
        this.jsonb = JsonbBuilder.create();
    }

    public void cacheRoom(Room room) {
        if (room == null) return;
        try {
            String key = ROOM_KEY_PREFIX + room.getNumber();
            RoomRedisEnt dto = new RoomRedisEnt(
                    room.getNumber(),
                    room.getCapacity(),
                    room.getBasePrice(),
                    room.getIsRented()
            );
            String json = jsonb.toJson(dto);
            jedis.jsonSet(key, json);
            jedis.expire(key, ROOM_TTL_SECONDS);
        } catch (JedisException e) {
            System.err.println(" Błąd zapisu do cache: " + e.getMessage());
        }
    }

    public Optional<Room> getRoomFromCache(int number) {
        try {
            String key = ROOM_KEY_PREFIX + number;
            RoomRedisEnt dto = jedis.jsonGet(key, RoomRedisEnt.class);
            if (dto != null) {
                return Optional.of(mapToDomain(dto));
            }
        } catch (JedisException e) {
            System.err.println(" Błąd odczytu z cache, przełączam na MongoDB: " + e.getMessage());
        } catch (jakarta.json.bind.JsonbException e) {
            System.err.println(" Błąd deserializacji JSON z cache, pomijam wpis: " + e.getMessage());
        }
        return Optional.empty();
    }

    public void invalidateRoom(int number) {
        try {
            jedis.jsonDel(ROOM_KEY_PREFIX + number);
            jedis.jsonDel(ALL_ROOMS_KEY);
        } catch (JedisException e) {
            System.err.println(" Błąd unieważniania klucza w Redis: " + e.getMessage());
        }
    }

    public void invalidateAll() {
        try {
            Set<String> keys = jedis.keys(ROOM_KEY_PREFIX + "*");
            if (keys != null && !keys.isEmpty()) {
                jedis.del(keys.toArray(new String[0]));
            }
            jedis.jsonDel(ALL_ROOMS_KEY);
        } catch (JedisException e) {
            System.err.println(" Błąd czyszczenia całego cache: " + e.getMessage());
        }
    }

    @Override
    public Optional<Room> findByNumber(int number) {
        Optional<Room> cachedRoom = getRoomFromCache(number);
        if (cachedRoom.isPresent()) {
            return cachedRoom;
        }

        Optional<Room> dbRoom = decoratedRepository.findByNumber(number);
        dbRoom.ifPresent(this::cacheRoom);
        return dbRoom;
    }

    @Override
    public void save(Room room) {
        decoratedRepository.save(room);
        invalidateRoom(room.getNumber());
    }

    @Override
    public List<Room> findAll() {
        try {
            Object rawJson = jedis.jsonGet(ALL_ROOMS_KEY);
            if (rawJson != null) {
                String jsonStr = rawJson.toString().trim();
                if (jsonStr.startsWith("[[")) {
                    RoomRedisEnt[][] nested = jsonb.fromJson(jsonStr, RoomRedisEnt[][].class);
                    if (nested != null && nested.length > 0 && nested[0] != null) {
                        List<Room> rooms = new ArrayList<>();
                        for (RoomRedisEnt dto : nested[0]) {
                            rooms.add(mapToDomain(dto));
                        }
                        return rooms;
                    }
                } else if (jsonStr.startsWith("[")) {
                    RoomRedisEnt[] dtos = jsonb.fromJson(jsonStr, RoomRedisEnt[].class);
                    if (dtos != null) {
                        List<Room> rooms = new ArrayList<>();
                        for (RoomRedisEnt dto : dtos) {
                            rooms.add(mapToDomain(dto));
                        }
                        return rooms;
                    }
                }
            }
        } catch (JedisException e) {
            System.err.println(" Błąd odczytu listy pokoi z Redis, przełączam na MongoDB: " + e.getMessage());
        }

        List<Room> roomsFromDb = decoratedRepository.findAll();

        try {
            List<RoomRedisEnt> dtos = roomsFromDb.stream()
                    .map(r -> new RoomRedisEnt(r.getNumber(), r.getCapacity(), r.getBasePrice(), r.getIsRented()))
                    .toList();
            jedis.jsonSet(ALL_ROOMS_KEY, jsonb.toJson(dtos));
            jedis.expire(ALL_ROOMS_KEY, ALL_ROOMS_TTL_SECONDS);
        } catch (JedisException e) {
            System.err.println(" Błąd zapisu listy pokoi do Redis: " + e.getMessage());
        }

        return roomsFromDb;
    }

    private Room mapToDomain(RoomRedisEnt dto) {
        Room room = new Room(dto.getNumber(), dto.getCapacity(), dto.getBasePrice());
        room.setIsRented(dto.getIsRented());
        return room;
    }
}