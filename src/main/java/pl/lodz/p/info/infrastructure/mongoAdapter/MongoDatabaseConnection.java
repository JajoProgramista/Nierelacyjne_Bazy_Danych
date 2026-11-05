package pl.lodz.p.info.infrastructure.mongoAdapter;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.Jsr310CodecProvider;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDatabaseConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    public static MongoClient getMongoClient() {
        if (mongoClient == null) {
            CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    CodecRegistries.fromProviders(new Jsr310CodecProvider()),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            String uri = "mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=rs0";

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri))
                    .codecRegistry(pojoCodecRegistry)
                    .writeConcern(WriteConcern.MAJORITY)
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("hoteldb");
        }
        return mongoClient;
    }

    public static MongoDatabase getDatabase() {
        getMongoClient();
        return database;
    }

    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            database = null;
        }
    }
}
