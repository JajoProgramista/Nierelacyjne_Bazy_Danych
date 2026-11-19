package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoDatabaseConnection;
import pl.lodz.p.info.infrastructure.mongoAdapter.MongoRoomRepositoryAdapter;
import pl.lodz.p.info.infrastructure.redisAdapter.RedisConnection;
import pl.lodz.p.info.infrastructure.redisAdapter.RedisRoomRepositoryDecorator;
import pl.lodz.p.info.model.Room;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class RoomCacheBenchmark {

    private MongoRoomRepositoryAdapter mongoAdapter;
    private RedisRoomRepositoryDecorator redisDecorator;

    @Setup(Level.Trial)
    public void setup() {
        mongoAdapter = new MongoRoomRepositoryAdapter();
        redisDecorator = new RedisRoomRepositoryDecorator(mongoAdapter);

        mongoAdapter.save(new Room(1, 2, 220.0));
        redisDecorator.findByNumber(1);
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        RedisConnection.close();
        MongoDatabaseConnection.close();
    }

    @Benchmark
    public Optional<Room> test1_ReadDirectlyFromMongo() {
        return mongoAdapter.findByNumber(1);
    }

    @Benchmark
    public Optional<Room> test2_ReadFromRedisCacheHit() {
        return redisDecorator.findByNumber(1);
    }

    @Benchmark
    public Optional<Room> test3_ReadWithCacheMissAndInvalidation() {
        redisDecorator.invalidateRoom(1);
        return redisDecorator.findByNumber(1);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(RoomCacheBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}