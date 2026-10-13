package pl.lodz.p.info.managers;

import pl.lodz.p.info.model.Address;
import pl.lodz.p.info.model.Client;
import pl.lodz.p.info.repositories.ClientRepository;
import java.util.List;
import java.util.function.Predicate;

public class ClientManager {
    private final ClientRepository clientRepository = new ClientRepository();

    public Client getClient(String personalID) {
        return clientRepository.findByPersonalId(personalID);
    }

    public Client registerClient(String firstName, String lastName, String personalID, Address address, String login, String password) {
        Client ptr = getClient(personalID);
        if (ptr != null) {
            return ptr;
        }
        Client nowy = new Client(firstName, lastName, personalID, address, login, password);
        clientRepository.add(nowy);
        return nowy;
    }

    public void unregisterClient(String personalID) {
        Client ptr = getClient(personalID);
        if (ptr != null) {
            ptr.setArchive(true);
        }
    }

    public List<Client> findClients(Predicate<Client> predicate) {
        return clientRepository.findBy(client -> predicate.test(client) && !client.isArchive());
    }

    public List<Client> findAllClients() {
        return findClients(client -> true);
    }

    public List<Client> findAllClientsIncludingArchive() {
        return clientRepository.findAll();
    }
}