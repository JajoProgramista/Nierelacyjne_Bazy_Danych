package pl.lodz.p.info.model;

public class Client {
    private String firstName;
    private String lastName;
    private final String personalId;
    private boolean archive;
    private Address address;
    private ClientType clientType;
    private String login;
    private String password;

    public Client(String firstName, String lastName, String personalId, Address address, String login, String password) {
        if (firstName == null || firstName.isEmpty()) throw new IllegalArgumentException("Invalid First Name");
        if (lastName == null || lastName.isEmpty()) throw new IllegalArgumentException("Invalid Last Name");
        if (personalId == null || personalId.isEmpty()) throw new IllegalArgumentException("Invalid PersonalId");
        if (address == null) throw new IllegalArgumentException("Invalid Address");

        this.firstName = firstName;
        this.lastName = lastName;
        this.personalId = personalId;
        this.address = address;
        this.login = login != null ? login : "";
        this.password = password != null ? password : "";
        this.archive = false;
        this.clientType = new DefaultClientType();
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPersonalId() { return personalId; }
    public Address getAddress() { return address; }
    public boolean isArchive() { return archive; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }

    public void setFirstName(String firstName) {
        if (firstName != null && !firstName.isEmpty()) this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        if (lastName != null && !lastName.isEmpty()) this.lastName = lastName;
    }
    public void setArchive(boolean archive) { this.archive = archive; }
    public void setAddress(Address address) {
        if (address != null) this.address = address;
    }
    public void setClientType(ClientType clientType) {
        if (clientType != null) this.clientType = clientType;
    }
    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }

    public double applyDiscount(double price) {
        return clientType.applyDiscount(price);
    }

    public String getLetterRepresentingClientType() {
        return clientType.clientTypeToString();
    }

    public String getInfo() {
        return "DANE OSOBOWE:\n-------------\n" +
                "IMIE: " + firstName + "\n" +
                "NAZWISKO: " + lastName + "\n" +
                "PESEL: " + personalId + "\n" +
                "TYP KLIENTA: " + clientType.getInfo() + "\n";
    }
}
