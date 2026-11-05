package pl.lodz.p.info.infrastructure.mongoAdapter;

import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import pl.lodz.p.info.infrastructure.mongoEntity.ClientTypeMongoEnt;

public class ClientTypeCodecProvider implements CodecProvider {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        if (ClientTypeMongoEnt.class.isAssignableFrom(clazz)) {
            return (Codec<T>) new ClientTypeCodec();
        }
        return null;
    }
}
