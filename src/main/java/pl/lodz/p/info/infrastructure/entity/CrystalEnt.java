package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("crystal")
public class CrystalEnt extends ClientTypeEnt {

    public CrystalEnt() {}

}
