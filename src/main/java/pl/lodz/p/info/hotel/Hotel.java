package pl.lodz.p.info.hotel;

import pl.lodz.p.info.managers.ClientManager;
import pl.lodz.p.info.managers.RentManager;
import pl.lodz.p.info.managers.RoomManager;
import pl.lodz.p.info.model.Address;
import pl.lodz.p.info.model.Client;
import pl.lodz.p.info.model.Rent;
import pl.lodz.p.info.model.Room;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Hotel {
    private final ClientManager clientManager = new ClientManager();
    private final RoomManager roomManager = new RoomManager();
    private final RentManager rentManager = new RentManager();
    private final DataStorage dataStorage = new DataStorage();

    public String getPathForDataStorageForClients() { return dataStorage.getPathFileForClients(); }
    public String getPathForDataStorageForRooms() { return dataStorage.getPathFileForRooms(); }
    public String getPathForDataStorageForCurrentRents() { return dataStorage.getPathFileForCurrentRents(); }
    public String getPathForDataStorageForArchiveRents() { return dataStorage.getPathFileForArchiveRents(); }

    public void setPathForDataStorageForClients(String path) { dataStorage.setPathToFileForClients(path); }
    public void setPathForDataStorageForRooms(String path) { dataStorage.setPathToFileForRooms(path); }
    public void setPathForDataStorageForCurrentRents(String path) { dataStorage.setPathToFileForCurrentRents(path); }
    public void setPathForDataStorageForArchiveRents(String path) { dataStorage.setPathToFileForArchiveRents(path); }

    public boolean saveData() {
        return dataStorage.saveClientsToFile(dataStorage.getPathFileForClients(), clientManager.findAllClientsIncludingArchive()) &&
                dataStorage.saveRoomsToFile(dataStorage.getPathFileForRooms(), roomManager.findAllRooms()) &&
                dataStorage.saveRentsToFile(dataStorage.getPathFileForCurrentRents(), rentManager.findAllRents()) &&
                dataStorage.saveRentsToFile(dataStorage.getPathFileForArchiveRents(), rentManager.findAllArchiveRents());
    }

    public void restoreAllDataFromFiles() {
        restoreClientsFromFile();
        restoreRoomsFromFile();
        restoreRentsFromFile(true);
        restoreRentsFromFile(false);
    }

    public void restoreClientsFromFile() {
        List<String> clientsData = dataStorage.loadDataFromFile(dataStorage.getPathFileForClients());
        List<Client> clientsRestoredFromFile = dataStorage.createClientsFromFile(clientsData);
        for (Client c : clientsRestoredFromFile) {
            clientManager.registerClient(c.getFirstName(), c.getLastName(), c.getPersonalId(), c.getAddress(), c.getLogin(), c.getPassword());
        }
    }

    public void restoreRoomsFromFile() {
        List<String> roomsData = dataStorage.loadDataFromFile(dataStorage.getPathFileForRooms());
        List<Room> roomsRestoredFromFile = dataStorage.createRoomsFromFile(roomsData);
        for (Room r : roomsRestoredFromFile) {
            roomManager.registerRoom(r.getNumber(), r.getCapacity(), r.getBasePrice());
        }
    }

    public void restoreRentsFromFile(boolean current) {
        List<String> rentsData = current ?
                dataStorage.loadDataFromFile(dataStorage.getPathFileForCurrentRents()) :
                dataStorage.loadDataFromFile(dataStorage.getPathFileForArchiveRents());

        List<String> clientsAndRoomDataForRents = dataStorage.getClientsAndRoomsIdForRentsFromFile(rentsData);
        List<Client> clientsForRents = new ArrayList<>();
        List<Room> roomsForRents = new ArrayList<>();

        for (int i = 0; i < clientsAndRoomDataForRents.size(); i += 2) {
            clientsForRents.add(clientManager.getClient(clientsAndRoomDataForRents.get(i)));
            roomsForRents.add(roomManager.getRoom(Integer.parseInt(clientsAndRoomDataForRents.get(i + 1))));
        }

        List<Rent> rents = dataStorage.createRentsFromFile(rentsData, clientsForRents, roomsForRents);
        for (Rent rent : rents) {
            if (!current) {
                rent.endRent();
            }
            rentManager.addRent(rent, current);
        }
    }

    public Room getRoom(int number) {
        return roomManager.getRoom(number);
    }

    public Room registerRoom(int number, int capacity, double basePrice) {
        return roomManager.registerRoom(number, capacity, basePrice);
    }

    public List<Room> findRooms(Predicate<Room> predicate) {
        return roomManager.findRooms(predicate);
    }

    public List<Room> findAllRooms() {
        return roomManager.findAllRooms();
    }

    public Client getClient(String clientPersonalID) {
        return clientManager.getClient(clientPersonalID);
    }

    public Client registerClient(String firstName, String lastName, String personalID, Address address, String login, String password) {
        return clientManager.registerClient(firstName, lastName, personalID, address, login, password);
    }

    public void unregisterClient(String clientPersonalID) {
        clientManager.unregisterClient(clientPersonalID);
    }

    public List<Client> findClients(Predicate<Client> predicate) {
        return clientManager.findClients(predicate);
    }

    public List<Client> findAllClients() {
        return clientManager.findAllClients();
    }

    public List<Client> findAllClientsIncludingArchive() {
        return clientManager.findAllClientsIncludingArchive();
    }

    public Rent rentRoom(Client client, Room room, LocalDateTime beginDate, LocalDateTime endDate) {
        return rentManager.rentRoom(client, room, beginDate, endDate);
    }

    public void returnRoom(Room room) {
        rentManager.returnRoom(room);
    }

    public List<Rent> findRents(Predicate<Rent> predicate) {
        return rentManager.findRents(predicate);
    }

    public List<Rent> findAllRents() {
        return rentManager.findAllRents();
    }

    public List<Rent> findAllArchiveRents() {
        return rentManager.findAllArchiveRents();
    }

    public void autoReturnRooms() {
        rentManager.autoReturnRooms();
    }

    public double checkClientRentBalance(Client client) {
        return rentManager.checkClientRentBalance(client);
    }

    public List<Rent> getClientRents(Client client) {
        return rentManager.getClientRents(client);
    }

    public List<Rent> getClientArchiveRents(Client client) {
        return rentManager.getClientArchiveRents(client);
    }

    public List<Room> getAvailableRooms(LocalDateTime beginDate, LocalDateTime endDate) {
        return roomManager.getAvailableRooms(beginDate, endDate, rentManager);
    }

    public void deleteRent(Rent rent) {
        rentManager.deleteRent(rent);
    }

    public void generateRooms() {
        roomManager.generateRooms();
    }
}
