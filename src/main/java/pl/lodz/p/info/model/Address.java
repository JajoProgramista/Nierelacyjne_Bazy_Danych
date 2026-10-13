package pl.lodz.p.info.model;

public class Address {
    private final String city;
    private final String street;
    private final String houseNumber;
    private final String number;
    private final String postcode;

    public Address(String city, String postcode, String street, String houseNumber, String number) {
        if (city == null || city.isEmpty()) throw new IllegalArgumentException("Invalid city");
        if (street == null || street.isEmpty()) throw new IllegalArgumentException("Invalid street");
        if (houseNumber == null || houseNumber.isEmpty()) throw new IllegalArgumentException("Invalid house number");
        if (postcode == null || postcode.isEmpty()) throw new IllegalArgumentException("Invalid postcode");

        this.city = city;
        this.postcode = postcode;
        this.street = street;
        this.houseNumber = houseNumber;
        this.number = number == null ? "" : number;
    }

    public String getCity() { return city; }
    public String getStreet() { return street; }
    public String getHouseNumber() { return houseNumber; }
    public String getNumber() { return number; }
    public String getPostcode() { return postcode; }

    public String getInfo() {
        StringBuilder ss = new StringBuilder();
        ss.append("DANE ADRESOWE: \n--------------\n");
        ss.append("MIASTO: ").append(city).append("\n");
        ss.append("ULICA: ").append(street).append("\n");
        ss.append("NUMER BUDYNKU: ").append(houseNumber).append("\n");
        if (!"0".equals(number) && !number.isEmpty()) {
            ss.append("NUMER MIESZKANIA: ").append(number).append("\n");
        }
        ss.append("KOD POCZTOWY: ").append(postcode).append("\n");
        return ss.toString();
    }
}
