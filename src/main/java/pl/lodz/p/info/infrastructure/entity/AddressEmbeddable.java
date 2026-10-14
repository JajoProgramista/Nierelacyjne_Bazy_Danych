package pl.lodz.p.info.infrastructure.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import java.io.Serializable;

@Embeddable
@Access(AccessType.FIELD)
public class AddressEmbeddable implements Serializable {

    @NotEmpty
    @Column(name = "CITY")
    private String city;

    @NotEmpty
    @Column(name = "POSTCODE")
    private String postcode;

    @NotEmpty
    @Column(name = "STREET")
    private String street;

    @NotEmpty
    @Column(name = "HOUSE_NUMBER")
    private String houseNumber;

    @Column(name = "APARTMENT_NUMBER")
    private String apartmentNumber;


    public AddressEmbeddable() {}

    public AddressEmbeddable(String city, String postcode, String street, String houseNumber, String apartmentNumber) {
        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getHouseNumber() { return houseNumber; }
    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }

    public String getApartmentNumber() { return apartmentNumber; }
    public void setApartmentNumber(String apartmentNumber) { this.apartmentNumber = apartmentNumber; }
}
