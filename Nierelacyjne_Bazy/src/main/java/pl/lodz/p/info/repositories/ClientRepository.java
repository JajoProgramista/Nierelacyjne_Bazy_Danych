package pl.lodz.p.info.repositories;

import pl.lodz.p.info.model.Client;
import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Optional<Client> findByPersonalId(String personalId);
    void save(Client client);
    List<Client> findAll();
}