package pl.futurecollars.invoicing.db.mongo;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.bson.Document;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.IdInterface;

@AllArgsConstructor
public class MongoBasedDatabase<T extends IdInterface> implements Database<T> {

    private MongoCollection<T> types;
    private MongoIdService idService;

    @Override
    public long save(T type) {

        type.setId(idService.getNextIdAndIncrement());
        types.insertOne(type);

        return type.getId();
    }

    @Override
    public Optional<T> getById(long id) {
        return Optional.ofNullable(
          types.find(idFilter(id)).first()
        );
    }

    @Override
    public List<T> getAll() {
        return StreamSupport
          .stream(types.find().spliterator(), false)
          .collect(Collectors.toList());
    }

    @Override
    public Optional<T> update(long id, T updatedType) {
        updatedType.setId(id);
        return Optional.ofNullable(
          types.findOneAndReplace(idFilter(id), updatedType)
        );
    }

    @Override
    public Optional<T> delete(long id) {
        return Optional.ofNullable(
          types.findOneAndDelete(idFilter(id))
        );
    }

    private Document idFilter(long id) {
        return new Document("_id", id);
    }
}
