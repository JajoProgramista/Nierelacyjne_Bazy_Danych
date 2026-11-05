package pl.lodz.p.info.infrastructure.mongoAdapter;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pl.lodz.p.info.infrastructure.mongoEntity.*;

public class ClientTypeCodec implements Codec<ClientTypeMongoEnt> {

    @Override
    public ClientTypeMongoEnt decode(BsonReader bsonReader, DecoderContext decoderContext) {
        bsonReader.readStartDocument();
        String type = "default";

        while (bsonReader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = bsonReader.readName();
            if ("_clazz".equals(name)) {
                type = bsonReader.readString();
            } else {
                bsonReader.skipValue();
            }
        }
        bsonReader.readEndDocument();

        return switch (type) {
            case "silver" -> new SilverMongoEnt();
            case "gold" -> new GoldMongoEnt();
            case "crystal" -> new CrystalMongoEnt();
            case "emerald" -> new EmeraldMongoEnt();
            default -> new DefaultClientTypeMongoEnt();
        };
    }

    @Override
    public void encode(BsonWriter bsonWriter, ClientTypeMongoEnt value, EncoderContext encoderContext) {
        bsonWriter.writeStartDocument();
        bsonWriter.writeName("_clazz");

        if (value instanceof SilverMongoEnt) bsonWriter.writeString("silver");
        else if (value instanceof GoldMongoEnt) bsonWriter.writeString("gold");
        else if (value instanceof CrystalMongoEnt) bsonWriter.writeString("crystal");
        else if (value instanceof EmeraldMongoEnt) bsonWriter.writeString("emerald");
        else bsonWriter.writeString("default");

        bsonWriter.writeEndDocument();
    }

    @Override
    public Class<ClientTypeMongoEnt> getEncoderClass() {
        return ClientTypeMongoEnt.class;
    }
}
