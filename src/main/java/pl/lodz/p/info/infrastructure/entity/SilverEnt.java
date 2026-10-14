package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("silver")
public class SilverEnt extends ClientTypeEnt {
    public SilverEnt() {}
}
