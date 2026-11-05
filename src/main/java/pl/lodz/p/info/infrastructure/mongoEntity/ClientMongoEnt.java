package pl.lodz.p.info.infrastructure.mongoEntity;

import org.bson.codecs.pojo.annotations.BsonId;

public class ClientMongoEnt {
    @BsonId
    private String personalId;
    private String firstName;
    private String lastName;
    private boolean archive;
    private String login;
    private String password;
    private AddressMongoEnt address;
    private ClientTypeMongoEnt clientType;

    public ClientMongoEnt() {
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
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

    public boolean isArchive() {
        return archive;
    }

    public void setArchive(boolean archive) {
        this.archive = archive;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AddressMongoEnt getAddress() {
        return address;
    }

    public void setAddress(AddressMongoEnt address) {
        this.address = address;
    }

    public ClientTypeMongoEnt getClientType() { return clientType; }
    public void setClientType(ClientTypeMongoEnt clientType) { this.clientType = clientType; }
}
