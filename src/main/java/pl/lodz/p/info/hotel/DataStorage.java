package pl.lodz.p.info.hotel;

import pl.lodz.p.info.model.Address;
import pl.lodz.p.info.model.Client;
import pl.lodz.p.info.model.Crystal;
import pl.lodz.p.info.model.DefaultClientType;
import pl.lodz.p.info.model.Emerald;
import pl.lodz.p.info.model.Gold;
import pl.lodz.p.info.model.Rent;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.model.Silver;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    private String pathToFileForClients = "Clients.csv";
    private String pathToFileForRooms = "Rooms.csv";
    private String pathToFileForCurrentRents = "CurrentRents.csv";
    private String pathToFileForArchiveRents = "ArchiveRents.csv";

    public DataStorage() {}

    public String getPathFileForClients() { return pathToFileForClients; }
    public String getPathFileForRooms() { return pathToFileForRooms; }
    public String getPathFileForCurrentRents() { return pathToFileForCurrentRents; }
    public String getPathFileForArchiveRents() { return pathToFileForArchiveRents; }

    public void setPathToFileForClients(String path) { if (path != null && !path.isEmpty()) this.pathToFileForClients = path; }
    public void setPathToFileForRooms(String path) { if (path != null && !path.isEmpty()) this.pathToFileForRooms = path; }
    public void setPathToFileForCurrentRents(String path) { if (path != null && !path.isEmpty()) this.pathToFileForCurrentRents = path; }
    public void setPathToFileForArchiveRents(String path) { if (path != null && !path.isEmpty()) this.pathToFileForArchiveRents = path; }

    public String clientDataToStringForCSV(Client client) {
        return client.getFirstName() + ";" + client.getLastName() + ";" + client.getPersonalId() + ";" +
                (client.isArchive() ? "1" : "0") + ";" + client.getLetterRepresentingClientType().charAt(0) + ";" +
                client.getLogin() + ";" + client.getPassword() + ";";
    }

    public String clientDataWithAddressToStringForCSV(Client client) {
        return clientDataToStringForCSV(client) + addressDataToStringForCSV(client.getAddress());
    }

    public String addressDataToStringForCSV(Address address) {
        return address.getCity() + ";" + address.getPostcode() + ";" + address.getStreet() + ";" +
                address.getHouseNumber() + ";" + address.getNumber() + ";\n";
    }

    public String roomDataToStringForCSV(Room room) {
        return room.getNumber() + ";" + room.getCapacity() + ";" + room.getBasePrice() + "\n";
    }

    public String rentDataToStringForCSV(Rent rent) {
        return rent.getId() + ";" + rent.getBeginDate().toString() + ";" + rent.getEndDate().toString() + ";" +
                rent.getClient().getPersonalId() + ";" + rent.getRoom().getNumber() + ";" +
                (rent.getIsCanceled() ? "1" : "0") + "\n";
    }

    public boolean saveClientsToFile(String path, List<Client> clients) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Client c : clients) sb.append(clientDataWithAddressToStringForCSV(c));
            Files.writeString(Paths.get(path), sb.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveRentsToFile(String path, List<Rent> rents) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Rent r : rents) sb.append(rentDataToStringForCSV(r));
            Files.writeString(Paths.get(path), sb.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean saveRoomsToFile(String path, List<Room> rooms) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Room r : rooms) sb.append(roomDataToStringForCSV(r));
            Files.writeString(Paths.get(path), sb.toString());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public List<String> loadDataFromFile(String path) {
        try {
            if (Files.exists(Paths.get(path))) {
                return Files.readAllLines(Paths.get(path));
            }
        } catch (IOException e) {
            // Zwraca pusta liste, jesli wystapi blad
        }
        return new ArrayList<>();
    }

    public Address createAddress(String[] tab, int offset) {
        String number = tab.length > offset + 4 ? tab[offset + 4] : "";
        return new Address(tab[offset], tab[offset + 1], tab[offset + 2], tab[offset + 3], number);
    }

    public Client createClientAndAddress(String line) {
        String[] tab = line.split(";", -1);
        Address address = createAddress(tab, 7);
        Client client = new Client(tab[0], tab[1], tab[2], address, tab[5], tab[6]);
        client.setArchive("1".equals(tab[3]));

        if (!tab[4].isEmpty()) {
            switch (tab[4].charAt(0)) {
                case 's': client.setClientType(new Silver()); break;
                case 'g': client.setClientType(new Gold()); break;
                case 'c': client.setClientType(new Crystal()); break;
                case 'e': client.setClientType(new Emerald()); break;
                default: client.setClientType(new DefaultClientType()); break;
            }
        }
        return client;
    }

    public Room createRoom(String line) {
        String[] tab = line.split(";", -1);
        return new Room(Integer.parseInt(tab[0]), Integer.parseInt(tab[1]), Double.parseDouble(tab[2]));
    }

    public List<Client> createClientsFromFile(List<String> data) {
        List<Client> result = new ArrayList<>();
        for (String line : data) {
            if (!line.trim().isEmpty()) {
                result.add(createClientAndAddress(line));
            }
        }
        return result;
    }

    public List<Room> createRoomsFromFile(List<String> data) {
        List<Room> result = new ArrayList<>();
        for (String line : data) {
            if (!line.trim().isEmpty()) {
                result.add(createRoom(line));
            }
        }
        return result;
    }

    public List<String> getClientsAndRoomsIdForRentsFromFile(List<String> data) {
        List<String> result = new ArrayList<>();
        for (String line : data) {
            if (!line.trim().isEmpty()) {
                String[] tab = line.split(";", -1);
                result.add(tab[3]); // personalId
                result.add(tab[4]); // numer pokoju
            }
        }
        return result;
    }

    public Rent createRent(String data, Client client, Room room) {
        String[] tab = data.split(";", -1);
        Rent rent = new Rent(Integer.parseInt(tab[0]), LocalDateTime.parse(tab[1]), LocalDateTime.parse(tab[2]), room, client);
        if ("1".equals(tab[5])) {
            rent.setIsCanceled(true);
        }
        return rent;
    }

    public List<Rent> createRentsFromFile(List<String> data, List<Client> clients, List<Room> rooms) {
        List<Rent> result = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).trim().isEmpty()) {
                result.add(createRent(data.get(i), clients.get(i), rooms.get(i)));
            }
        }
        return result;
    }
}
