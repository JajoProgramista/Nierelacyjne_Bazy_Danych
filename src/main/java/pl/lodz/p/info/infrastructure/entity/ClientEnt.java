package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Access(AccessType.FIELD)
public class ClientEnt extends AbstractEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Embedded
    @AttributeOverride(name = "personalId", column = @Column(name = "PESEL", unique = true))
    @NotNull
    private PersonalIdEmbeddable personId;

    @NotEmpty
    @Column(name = "FIRSTNAME")
    private String firstName;

    @NotEmpty
    @Column(name = "LASTNAME")
    private String lastName;

    private boolean archive;
    private String login;
    private String password;

    @Embedded
    private AddressEmbeddable address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "client_type_id")
    @NotNull
    private ClientTypeEnt clientType;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<RentEnt> rents = new ArrayList<>();

    public ClientEnt() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonalIdEmbeddable getPersonId() {
        return personId;
    }

    public void setPersonId(PersonalIdEmbeddable personId) {
        this.personId = personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AddressEmbeddable getAddress() {
        return address;
    }

    public void setAddress(AddressEmbeddable address) {
        this.address = address;
    }

    public List<RentEnt> getRents() {
        return rents;
    }

    public void setRents(List<RentEnt> rents) {
        this.rents = rents;
    }

    public ClientTypeEnt getClientType() {
        return clientType;
    }

    public void setClientType(ClientTypeEnt clientType) {
        this.clientType = clientType;
    }
}