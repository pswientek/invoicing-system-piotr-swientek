package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@AllArgsConstructor
public class FileBasedDatabase implements Database {

    private IdService idService;
    private FileService fileService;
    private FileService idFileService;
    private JsonService jsonService;

    @Override
    public long save(Invoice invoice) {
        try {
            invoice.setId(idService.getNextIdAndIncrement(idFileService));
            fileService.appendLineToFile(jsonService.objectAsJson(invoice));

            return invoice.getId();
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to save invoice", e);
        }
    }

    @Override
    public Optional<Invoice> getById(long id) {
        try {
            return fileService.readAllLines()
                    .stream()
                    .filter(line -> containsId(line, id))
                    .map(line -> jsonService.returnJsonAsInvoice(line, Invoice.class))
                    .findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to get invoice with id: " + id, e);
        }
    }

    @Override
    public List<Invoice> getAll() {
        try {
            return fileService.readAllLines()
                    .stream()
                    .map(line -> jsonService.returnJsonAsInvoice(line, Invoice.class))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to read invoices from file", e);
        }
    }

    @Override
    public Optional<Invoice> update(long id, Invoice updatedInvoice) {
        try {
            List<String> allInvoices = fileService.readAllLines();
            var listWithoutInvoiceWithGivenId = allInvoices
                    .stream()
                    .filter(line -> !containsId(line, id))
                    .collect(Collectors.toList());

            updatedInvoice.setId(id);
            listWithoutInvoiceWithGivenId.add(jsonService.objectAsJson(updatedInvoice));

            fileService.updateFile(listWithoutInvoiceWithGivenId);

            allInvoices.removeAll(listWithoutInvoiceWithGivenId);
            if (allInvoices.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(jsonService.returnJsonAsInvoice(allInvoices.get(0), Invoice.class));
        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to update invoice with id: " + id, e);
        }
    }

    @Override
    public Optional<Invoice> delete(long id) {
        try {
            var allInvoices = fileService.readAllLines();

            var updatedList = allInvoices
                    .stream()
                    .filter(line -> !containsId(line, id))
                    .collect(Collectors.toList());

            fileService.updateFile(updatedList);

            allInvoices.removeAll(updatedList);

            if (allInvoices.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(jsonService.returnJsonAsInvoice(allInvoices.get(0), Invoice.class));

        } catch (IOException e) {
            throw new RuntimeException("Database error: failed to delete invoice with id: " + id, e);
        }
    }

    private boolean containsId(String line, long id) {
        return line.contains("\"id\":" + id + ",\"number\"");
    }
}
