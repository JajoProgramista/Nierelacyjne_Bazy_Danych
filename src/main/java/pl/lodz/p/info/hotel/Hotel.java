package pl.lodz.p.info.hotel;

import pl.lodz.p.info.managers.ClientManager;
import pl.lodz.p.info.managers.RentManager;
import pl.lodz.p.info.managers.RoomManager;
import pl.lodz.p.info.model.Address;
import pl.lodz.p.info.model.Client;
import pl.lodz.p.info.model.Rent;
import pl.lodz.p.info.model.Room;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

public class Hotel {
    private final ClientManager clientManager = new ClientManager();
    private final RoomManager roomManager = new RoomManager();
    private final RentManager rentManager = new RentManager();

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