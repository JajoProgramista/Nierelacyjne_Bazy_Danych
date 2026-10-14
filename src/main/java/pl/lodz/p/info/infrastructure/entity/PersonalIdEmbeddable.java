package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

@Embeddable
@Access(AccessType.FIELD)
public class PersonalIdEmbeddable implements Serializable {

    @NotEmpty
    private String personalId;

    public PersonalIdEmbeddable() {}

    public PersonalIdEmbeddable(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalId() { return personalId; }
    public void setPersonalId(String personalId) { this.personalId = personalId; }
}
