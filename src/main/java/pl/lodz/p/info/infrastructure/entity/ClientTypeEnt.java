package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "client_types")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE")
@Access(AccessType.FIELD)
public abstract class ClientTypeEnt extends AbstractEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    public ClientTypeEnt() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
