package pl.lodz.p.info.infrastructure.redisAdapter;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

public class RedisConnection {
    private static JedisPooled pool;

    public static JedisPooled getJedisPooled() {
        if (pool == null) {
            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                    .connectionTimeoutMillis(1000)
                    .socketTimeoutMillis(1000)
                    .build();
            pool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);
        }
        return pool;
    }

    public static void close() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }
}
