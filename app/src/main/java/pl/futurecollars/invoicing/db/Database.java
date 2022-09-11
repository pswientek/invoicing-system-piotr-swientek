package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.IdInterface;

public interface Database<T extends IdInterface> {

    long save(T type);

    Optional<T> getById(long id);

    List<T> getAll();

    Optional<T> update(long id, T updatedType);

    Optional<T> delete(long id);

    default void reset() {
        getAll().forEach(item -> delete(item.getId()));
    }

}
