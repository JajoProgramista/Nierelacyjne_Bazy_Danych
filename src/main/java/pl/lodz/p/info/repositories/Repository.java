package pl.lodz.p.info.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Repository<T> {
    private final List<T> elements = new ArrayList<>();

    public T get(int index) {
        if (elements.isEmpty()) throw new IllegalStateException("Repository is Empty");
        if (index < 0 || index >= elements.size()) throw new IllegalArgumentException("Invalid Index");
        return elements.get(index);
    }

    public void add(T element) {
        if (element == null) throw new IllegalArgumentException("Invalid Pointer");
        elements.add(element);
    }

    public void remove(T element) {
        if (element == null) throw new IllegalArgumentException("Invalid Pointer");
        if (elements.isEmpty()) throw new IllegalStateException("Repository is Empty");
        elements.remove(element);
    }

    public int size() {
        return elements.size();
    }

    public List<T> findBy(Predicate<T> predicate) {
        return elements.stream().filter(predicate).collect(Collectors.toList());
    }

    public List<T> findAll() {
        return new ArrayList<>(elements);
    }

    public String report() {
        StringBuilder description = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            description.append(i).append(" ").append(elements.get(i).toString()).append("\n");
        }
        return description.toString();
    }
}