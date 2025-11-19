package pl.lodz.p;

import java.io.FileNotFoundException;

public interface Dao<T> {
    T read() throws FileNotFoundException;

    void write(T obj) throws FileNotFoundException;
}
