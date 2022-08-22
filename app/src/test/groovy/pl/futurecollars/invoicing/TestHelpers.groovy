package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        Company.builder()
                .taxIdentificationNumber("$id")
                .address("ul. Chabrowa 10b/$id 44-100 Rybnik, Polska")
                .name("SMA corp $id SA")
                .build()
    }

    static product(int id) {
        InvoiceEntry.builder()
                    .description("Antenna matching")
                    .quantity(1)
                    .price(BigDecimal.valueOf(id * 2000))
                    .vatValue(BigDecimal.valueOf(id * 2000 * 0.08))
                    .vatRate(Vat.VAT_8)
                    .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .date(LocalDate.now())
                .buyer(company(id + 10))
                .seller(company(id))
                .entries((1..id).collect({ product(it) }))
                .build()
    }
}