package pl.futurecollars.invoicing.service.company;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

@Service
public class CompanyRestService {

    private Database<Company> database;

    @Autowired
    public void setDatabase(Database<Company> database) {
        this.database = database;
    }

    public long save(Company company) {
        return database.save(company);
    }

    public ResponseEntity<Company> getById(long id) {
        return database.getById(id)
          .map(company -> ResponseEntity.ok().body(company))
          .orElse(ResponseEntity.notFound().build());
    }

    public List<Company> getAll() {
        return database.getAll();
    }

    public ResponseEntity<?> update(long id, Company company) {
        return database.update(id, company)
          .map(name -> ResponseEntity.noContent().build())
          .orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteById(long id) {
        return database.delete(id)
          .map(name -> ResponseEntity.noContent().build())
          .orElse(ResponseEntity.notFound().build());
    }
}
