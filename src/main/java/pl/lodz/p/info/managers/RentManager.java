package pl.lodz.p.info.managers;

import pl.lodz.p.info.infrastructure.adapter.JpaRentRepositoryAdapter;
import pl.lodz.p.info.model.Client;
import pl.lodz.p.info.model.Crystal;
import pl.lodz.p.info.model.Emerald;
import pl.lodz.p.info.model.Gold;
import pl.lodz.p.info.model.Rent;
import pl.lodz.p.info.model.Room;
import pl.lodz.p.info.model.Silver;
import pl.lodz.p.info.repositories.RentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RentManager {
    private final RentRepository rentRepository = new JpaRentRepositoryAdapter();
    private final ClientManager clientManager = new ClientManager();

    public List<Rent> getClientRents(Client client) {
        return rentRepository.findAll().stream()
                .filter(rent -> rent.getClient().getPersonalId().equals(client.getPersonalId())
                        && !rent.getIsCanceled()
                        && rent.getEndDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public Rent getRoomRents(Room room) {
        return rentRepository.findAll().stream()
                .filter(rent -> rent.getRoom().getNumber() == room.getNumber()
                        && !rent.getIsCanceled()
                        && rent.getEndDate().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElse(null);
    }

    public List<Rent> findRents(Predicate<Rent> predicate) {
        return rentRepository.findAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public List<Rent> findAllRents() {
        return rentRepository.findAll().stream()
                .filter(rent -> !rent.getIsCanceled() && rent.getEndDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public double checkClientRentBalance(Client client) {
        double balance = 0;
        List<Rent> clientArchiveRents = getClientArchiveRents(client);
        for (Rent rent : clientArchiveRents) {
            balance += rent.getRentCost();
        }
        return balance;
    }

    public Rent rentRoom(Client client, Room room, LocalDateTime beginDate, LocalDateTime endDate) {
        if (client.isArchive()) {
            throw new IllegalStateException("Client is archive");
        }

        List<Rent> currentRentsForRoom = rentRepository.findAll().stream()
                .filter(rent -> rent.getRoom().getNumber() == room.getNumber() && !rent.getIsCanceled())
                .collect(Collectors.toList());

        boolean isOK = true;

        for (Rent rent : currentRentsForRoom) {
            if ((endDate.compareTo(rent.getEndDate()) <= 0 && endDate.compareTo(rent.getBeginDate()) > 0) ||
                    (beginDate.compareTo(rent.getEndDate()) < 0 && beginDate.compareTo(rent.getBeginDate()) >= 0) ||
                    (endDate.compareTo(rent.getEndDate()) >= 0 && beginDate.compareTo(rent.getBeginDate()) <= 0)) {
                isOK = false;
                break;
            }
        }

        if (!isOK) {
            throw new IllegalStateException("The room has been already rented.");
        }

        // Poprawne wyliczanie unikalnego Business ID
        int newRentId = rentRepository.findAll().stream()
                .mapToInt(Rent::getId)
                .max()
                .orElse(0) + 1;

        Rent ptr = new Rent(newRentId, beginDate, endDate, room, client);
        rentRepository.save(ptr);
        return ptr;
    }

    public void returnRoom(Room room) {
        Rent ptr = getRoomRents(room);
        if (ptr == null) {
            throw new IllegalArgumentException("Rent does not exist or has already been ended");
        }
        ptr.endRent();
        rentRepository.save(ptr);

        changeClientType(ptr.getClient());
    }

    public void changeClientType(Client client) {
        double balance = checkClientRentBalance(client);
        if (balance >= 25000 && balance < 50000) {
            client.setClientType(new Silver());
        }
        if (balance >= 50000 && balance < 75000) {
            client.setClientType(new Gold());
        }
        if (balance >= 75000 && balance < 100000) {
            client.setClientType(new Crystal());
        }
        if (balance >= 100000) {
            client.setClientType(new Emerald());
        }

        clientManager.registerClient(client.getFirstName(), client.getLastName(), client.getPersonalId(),
                client.getAddress(), client.getLogin(), client.getPassword());
    }

    public List<Rent> getClientArchiveRents(Client client) {
        return rentRepository.findAll().stream()
                .filter(rent -> rent.getClient().getPersonalId().equals(client.getPersonalId())
                        && !rent.getIsCanceled()
                        && rent.getEndDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }

    public void autoReturnRooms() {
        List<Rent> currentRents = findAllRents();
        for (Rent rent : currentRents) {
            if (rent.getEndDate().isBefore(LocalDateTime.now())) {
                returnRoom(rent.getRoom());
            }
        }
    }

    public void deleteRent(Rent rent) {
        rent.setIsCanceled(true);
        rentRepository.save(rent);
    }

    public void addRent(Rent rent, boolean current) {
        rentRepository.save(rent);
    }

    public List<Rent> findAllArchiveRents() {
        return rentRepository.findAll().stream()
                .filter(rent -> !rent.getIsCanceled() && rent.getEndDate().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}