package pl.futurecollars.invoicing.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class InvoiceService {

    private Database database;

    public InvoiceService(@Qualifier("fileBasedDatabase") Database database) {
        this.database = database;
    }

    @Autowired
    public void setDatabase(Database database) {
        this.database = database;
    }

    public int save(Invoice invoice) {
        return database.save(invoice);
    }

    public ResponseEntity<Invoice> getById(int id) {
        return database.getById(id)
                .map(invoice -> ResponseEntity.ok().body(invoice))
                .orElse(ResponseEntity.notFound().build());
    }

    public List<Invoice> getAll() {
        return database.getAll();
    }

    public ResponseEntity<?> update(int id, Invoice invoice) {
        return database.update(id, invoice)
                .map(name -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteById(int id) {
        return database.delete(id)
                .map(name -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

}
