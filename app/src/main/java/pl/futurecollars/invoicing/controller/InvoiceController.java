package pl.futurecollars.invoicing.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.RestService;

@RestController
@AllArgsConstructor
public class InvoiceController implements InvoiceApi {

    private final RestService restService;

    @Override
    public int add(@RequestBody Invoice invoice) {
        return restService.save(invoice);
    }

    @Override
    public List<Invoice> getAll() {
        return restService.getAll();
    }

    @Override
    public ResponseEntity<Invoice> getById(@PathVariable int id) {
        return restService.getById(id);
    }

    @Override
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice invoice) {
        return restService.update(id, invoice);
    }

    @Override
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        return restService.deleteById(id);
    }

}
