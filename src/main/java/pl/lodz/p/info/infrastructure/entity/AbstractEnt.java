package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@MappedSuperclass
public abstract class AbstractEnt implements Serializable {

    @Column(name = "ENTITYID", updatable = false, nullable = false)
    private UUID entityId = UUID.randomUUID();

    @Version
    @Column(name = "VERSION")
    private long version;

    public UUID getEntityId() { return entityId; }
    public long getVersion() { return version; }
}
