package pl.lodz.p;

import java.io.FileNotFoundException;
import java.sql.SQLException;

public interface Dao<T> {
    T read() throws FileNotFoundException, SQLException;

    void write(T obj) throws FileNotFoundException, SQLException;
}
