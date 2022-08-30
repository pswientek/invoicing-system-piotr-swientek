package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.IdInterface;

public class InMemoryDatabase<T extends IdInterface> implements Database<T> {

    private final Map<Long, T> types = new HashMap<>();
    private long nextId = 1;

    @Override
    public long save(T type) {
        type.setId(nextId);
        types.put(nextId, type);

        return nextId++;
    }

    @Override
    public Optional<T> getById(long id) {
        return Optional.ofNullable(types.get(id));
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(types.values());
    }

    @Override
    public Optional<T> update(long id, T updatedType) {
        updatedType.setId(id);
        return Optional.ofNullable(types.put(id, updatedType));
    }

    @Override
    public Optional<T> delete(long id) {
        return Optional.ofNullable(types.remove(id));
    }
}
