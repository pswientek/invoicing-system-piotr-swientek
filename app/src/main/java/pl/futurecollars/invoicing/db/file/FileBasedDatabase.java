package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.IdInterface;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@AllArgsConstructor
public class FileBasedDatabase<T extends IdInterface> implements Database<T> {

    private IdService idService;
    private FileService fileService;
    private FileService idFileService;
    private JsonService jsonService;
    private final Class<T> clazz;

    @Override
    public long save(T type) {
        try {
            type.setId(idService.getNextIdAndIncrement(idFileService));
            fileService.appendLineToFile(jsonService.objectAsJson(type));

            return type.getId();
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to save invoice", e);
        }
    }

    @Override
    public Optional<T> getById(long id) {
        try {
            return fileService.readAllLines()
                    .stream()
                    .filter(line -> containsId(line, id))
                    .map(line -> jsonService.returnJsonAsObject(line, clazz))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to get invoice with id: " + id, e);
        }
    }

    @Override
    public List<T> getAll() {
        try {
            return fileService.readAllLines()
                    .stream()
                    .map(line -> jsonService.returnJsonAsObject(line, clazz))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to read invoices from file", e);
        }
    }

    @Override
    public Optional<T> update(long id, T updatedType) {
        try {
            List<String> allTypes = fileService.readAllLines();
            var listWithoutTypeWithGivenId = allTypes
                    .stream()
                    .filter(line -> !containsId(line, id))
                    .collect(Collectors.toList());

            updatedType.setId(id);
            listWithoutTypeWithGivenId.add(jsonService.objectAsJson(updatedType));

            fileService.updateFile(listWithoutTypeWithGivenId);

            allTypes.removeAll(listWithoutTypeWithGivenId);
            if (allTypes.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(jsonService.returnJsonAsObject(allTypes.get(0), clazz));
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to update invoice with id: " + id, e);
        }
    }

    @Override
    public Optional<T> delete(long id) {
        try {
            var allTypes = fileService.readAllLines();

            var updatedList = allTypes
                    .stream()
                    .filter(line -> !containsId(line, id))
                    .collect(Collectors.toList());

            fileService.updateFile(updatedList);

            allTypes.removeAll(updatedList);

            if (allTypes.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(jsonService.returnJsonAsObject(allTypes.get(0), clazz));

        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to delete invoice with id: " + id, e);
        }
    }

    private boolean containsId(String line, long id) {
        return line.contains("\"id\":" + id + ",");
    }
}
