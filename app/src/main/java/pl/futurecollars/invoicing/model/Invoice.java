package pl.futurecollars.invoicing.model;

import java.time.LocalDate;
import java.util.List;

public class Invoice {

    private int id;
    private final LocalDate date;
    private final Company buyer;
    private final Company seller;
    private final List<InvoiceEntry> entries;

    public Invoice(LocalDate date, Company buyer, Company seller, List<InvoiceEntry> entries) {

        this.date = date;
        this.buyer = buyer;
        this.seller = seller;
        this.entries = entries;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
