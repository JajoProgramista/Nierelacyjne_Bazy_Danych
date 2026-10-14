package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("emerald")
public class EmeraldEnt extends ClientTypeEnt {

    public EmeraldEnt() {}

}
