package pl.futurecollars.invoicing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;
import pl.futurecollars.invoicing.service.InvoiceService;

public class App {

    public static void main(String[] args) {

        Database db = new InMemoryDatabase();
        InvoiceService service = new InvoiceService(db);

        Company buyer = new Company("1234565463", "SMA corp Sp. z o.o", "44-120 Rybnik, ul. Wyzwolenia 1");
        Company seller = new Company("555-666-66-11", "Słabe anteny SA", "44-100 Gliwice, ul. Graniczna 999");

        List<InvoiceEntry> products = List.of(new InvoiceEntry("Antenna matching", BigDecimal.valueOf(10000), BigDecimal.valueOf(2300), Vat.VAT_23));

        Invoice invoice = new Invoice(LocalDate.now(), buyer, seller, products);

        int id = service.save(invoice);

        service.getById(id).ifPresent(System.out::println);

        System.out.println(service.getAll());

        service.delete(id);
    }
}
