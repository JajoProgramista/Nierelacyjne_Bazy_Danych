package pl.lodz.p.info.infrastructure.redisAdapter;

import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPooled;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RedisConnection {
    private static JedisPooled pool;

    public static JedisPooled getJedisPooled() {
        if (pool == null) {
            Properties props = loadProperties();
            String host = props.getProperty("redis.host", "localhost");
            int port = Integer.parseInt(props.getProperty("redis.port", "6379"));
            int connTimeout = Integer.parseInt(props.getProperty("redis.connectionTimeoutMillis", "1000"));
            int sockTimeout = Integer.parseInt(props.getProperty("redis.socketTimeoutMillis", "1000"));

            JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                    .connectionTimeoutMillis(connTimeout)
                    .socketTimeoutMillis(sockTimeout)
                    .build();
            pool = new JedisPooled(new HostAndPort(host, port), clientConfig);
        }
        return pool;
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = RedisConnection.class.getClassLoader().getResourceAsStream("redis.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Nie udało się wczytać konfiguracji Redisa ", e);
        }
        return props;
    }

    public static void close() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }
}