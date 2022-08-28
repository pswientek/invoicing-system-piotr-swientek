package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Car
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(long id) {
        Company.builder()
                .taxIdentificationNumber("$id")
                .address("ul. Chabrowa 10b/$id 44-100 Rybnik, Polska")
                .name("SMA corp $id SA")
                .pensionInsurance((BigDecimal.TEN * BigDecimal.valueOf(id)).setScale(2))
                .healthInsurance((BigDecimal.valueOf(100) * BigDecimal.valueOf(id)).setScale(2))
                .build()
    }

    static product(long id) {
        InvoiceEntry.builder()
                    .description("Antenna matching")
                    .quantity(1.00)
                    .price((BigDecimal.valueOf(id * 2000)).setScale(2))
                    .vatValue((BigDecimal.valueOf(id * 2000 * 0.08)).setScale(2))
                    .vatRate(Vat.VAT_8)
                    .carExpenses(id % 2 == 0 ? null :
                            Car.builder()
                                    .registrationPlate("XYZ")
                                    .personalUsage(false)
                                    .build()
                    )
                    .build()
    }

    static invoice(long id) {
        Invoice.builder()
                .date(LocalDate.now())
                .number("2022/0101/2564/$id")
                .buyer(company(id + 10))
                .seller(company(id))
                .entries((1..id).collect({ product(it) }))
                .build()
    }

    static Invoice resetIds(Invoice invoice) {
        invoice.getBuyer().id = 0
        invoice.getSeller().id = 0
        invoice.entries.forEach {
            it.id = 0
            it.carExpenses?.id = 0
        }
        invoice
    }

    static List<Invoice> resetIds(List<Invoice> invoices) {
        invoices.forEach { invoice -> resetIds(invoice) }
    }

}