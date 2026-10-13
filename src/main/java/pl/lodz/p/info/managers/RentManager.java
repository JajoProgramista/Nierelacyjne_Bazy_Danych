package pl.lodz.p.info.managers;

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

public class RentManager {
    private final RentRepository currentRents = new RentRepository();
    private final RentRepository archiveRents = new RentRepository();

    public List<Rent> getClientRents(Client client) {
        return currentRents.findBy(rent -> rent.getClient().getPersonalId().equals(client.getPersonalId()));
    }

    public Rent getRoomRents(Room room) {
        List<Rent> result = currentRents.findBy(rent -> rent.getRoom().getNumber() == room.getNumber());
        if (result.isEmpty()) return null;
        return result.get(0);
    }

    public List<Rent> findRents(Predicate<Rent> predicate) {
        return currentRents.findBy(predicate);
    }

    public List<Rent> findAllRents() {
        return currentRents.findAll();
    }

    public double checkClientRentBalance(Client client) {
        double balance = 0;
        List<Rent> clientArchiveRents = archiveRents.findBy(rent ->
                rent.getClient().getPersonalId().equals(client.getPersonalId()) && !rent.getIsCanceled()
        );
        for (Rent rent : clientArchiveRents) {
            balance += rent.getRentCost();
        }
        return balance;
    }

    public Rent rentRoom(Client client, Room room, LocalDateTime beginDate, LocalDateTime endDate) {
        if (client.isArchive()) {
            throw new IllegalStateException("Client is archive");
        }

        List<Rent> currentRentsForRoom = currentRents.findBy(rent -> rent.getRoom().getNumber() == room.getNumber());
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

        int newRentId = archiveRents.size() + currentRents.size() + 1;
        Rent ptr = new Rent(newRentId, beginDate, endDate, room, client);
        currentRents.add(ptr);
        return ptr;
    }

    public void returnRoom(Room room) {
        Rent ptr = getRoomRents(room);
        if (ptr == null) {
            throw new IllegalArgumentException("Rent does not exist");
        }
        ptr.endRent();
        currentRents.remove(ptr);
        archiveRents.add(ptr);
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
    }

    public List<Rent> getClientArchiveRents(Client client) {
        return archiveRents.findBy(rent -> rent.getClient().getPersonalId().equals(client.getPersonalId()));
    }

    public void autoReturnRooms() {
        for (Rent rent : currentRents.findAll()) {
            if (rent.getEndDate().isBefore(LocalDateTime.now())) {
                returnRoom(rent.getRoom());
            }
        }
    }

    public void deleteRent(Rent rent) {
        currentRents.remove(rent);
        rent.setIsCanceled(true);
        archiveRents.add(rent);
    }

    public void addRent(Rent rent, boolean current) {
        if (current) {
            currentRents.add(rent);
        } else {
            archiveRents.add(rent);
        }
    }

    public List<Rent> findAllArchiveRents() {
        return archiveRents.findAll();
    }
}
