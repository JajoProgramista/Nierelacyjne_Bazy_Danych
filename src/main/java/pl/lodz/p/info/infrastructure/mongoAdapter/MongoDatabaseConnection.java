package pl.lodz.p.info.infrastructure.mongoAdapter;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationAction;
import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;
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
                    CodecRegistries.fromProviders(new ClientTypeCodecProvider()),
                    CodecRegistries.fromProviders(new Jsr310CodecProvider()),
                    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );

            String uri = "mongodb://mongo1:27017,mongo2:27018,mongo3:27019/?replicaSet=rs0";

            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(uri))
                    .codecRegistry(pojoCodecRegistry)
                    .writeConcern(WriteConcern.MAJORITY)
                    .build();

            mongoClient = MongoClients.create(settings);
            database = mongoClient.getDatabase("hoteldb");

            initializeRoomSchema();
        }
        return mongoClient;
    }

    private static void initializeRoomSchema() {
        boolean collectionExists = database.listCollectionNames().into(new java.util.ArrayList<>()).contains("rooms");
        if (!collectionExists) {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse("""
                    {
                        $jsonSchema: {
                            bsonType: "object",
                            required: ["isRented"],
                            properties: {
                                isRented: {
                                    bsonType: "int",
                                    minimum: 0,
                                    maximum: 1,
                                    description: "Wartość musi być 1 (wynajęty) lub 0 (wolny)"
                                }
                            }
                        }
                    }
                    """)
            ).validationAction(ValidationAction.ERROR);

            CreateCollectionOptions options = new CreateCollectionOptions().validationOptions(validationOptions);
            database.createCollection("rooms", options);
        }
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