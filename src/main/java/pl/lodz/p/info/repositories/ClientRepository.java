package pl.lodz.p.info.repositories;

import pl.lodz.p.info.model.Client;
import java.util.List;

public class ClientRepository extends Repository<Client> {
    public Client findByPersonalId(String personalId) {
        List<Client> tmp = findBy(client -> client.getPersonalId().equals(personalId));
        return tmp.isEmpty() ? null : tmp.get(0);
    }
}