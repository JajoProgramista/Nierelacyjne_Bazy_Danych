package pl.lodz.p.info.repositories;

import pl.lodz.p.info.model.Rent;
import java.util.List;
import java.util.Optional;

public interface RentRepository {
    Optional<Rent> findById(int id);
    void save(Rent rent);
    List<Rent> findAll();
    void delete(Rent rent);
}