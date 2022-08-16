package pl.futurecollars.invoicing.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.RestService;

@RestController
@RequestMapping("invoices")
public class InvoiceController {

    @Autowired
    private final RestService restService;

    @Autowired
    public InvoiceController(RestService restService) {
        this.restService = restService;
    }

    @PostMapping
    public int add(@RequestBody Invoice invoice) {
        return restService.save(invoice);
    }

    @GetMapping(produces = { "application/json;charset=UTF-8" })
    public List<Invoice> getAll() {
        return restService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getById(@PathVariable int id) {
        return restService.getById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Invoice invoice) {
        return restService.update(id, invoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable int id) {
        return restService.deleteById(id);
    }

}
