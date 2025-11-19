package pl.lodz.p;

public interface Dao<T> {
    public T read();
    public void write(T obj);
}
