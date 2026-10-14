package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("default")
public class DefaultClientTypeEnt extends ClientTypeEnt {
    public DefaultClientTypeEnt() {}
}
