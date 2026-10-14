package pl.lodz.p.info.repositories;

import pl.lodz.p.info.model.Rent;
import java.util.List;

public class RentRepository extends Repository<Rent> {
    public Rent findById(int id) {
        List<Rent> tmp = findBy(rent -> rent.getId() == id);
        return tmp.isEmpty() ? null : tmp.get(0);
    }
}