package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("gold")
public class GoldEnt extends ClientTypeEnt {

    public GoldEnt() {}

}
