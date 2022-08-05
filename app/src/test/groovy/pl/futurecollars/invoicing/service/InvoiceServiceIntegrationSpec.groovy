package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification
import static pl.futurecollars.invoicing.TestHelpers.invoice

class InvoiceServiceIntegrationSpec extends Specification {

    private InvoiceService service
    private List<Invoice> invoices

    def setup() {
        Database db = new InMemoryDatabase()
        service = new InvoiceService(db)

        invoices = (1..12).collect { invoice(it) }
    }

    def "should save invoices returning sequential id, invoice should have id set to correct value, get by id returns saved invoice"() {
        when:
        def ids = invoices.collect({ service.save(it) })

        then:
        ids == (1..invoices.size()).collect()
        ids.forEach({ assert service.getById(it).hasBody() })
        ids.forEach({ assert service.getById(it).getBody().getId() == it })
        ids.forEach({ assert service.getById(it).getBody() == invoices.get(it - 1) })
    }

    def "get all returns empty collection if there were no invoices"() {
        expect:
        service.getAll().isEmpty()
    }

    def "get all returns all invoices in the database, deleted invoice is not returned"() {
        given:
        invoices.forEach({ service.save(it) })

        expect:
        service.getAll().size() == invoices.size()
        service.getAll().forEach({ assert it == invoices.get(it.getId() - 1) })

        when:
        service.deleteById(1)

        then:
        service.getAll().size() == invoices.size() - 1
        service.getAll().forEach({ assert it == invoices.get(it.getId() - 1) })
        service.getAll().forEach({ assert it.getId() != 1 })
    }

    def "can delete all invoices"() {
        given:
        invoices.forEach({ service.save(it) })

        when:
        invoices.forEach({ service.deleteById(it.getId()) })

        then:
        service.getAll().isEmpty()
    }

    def "it's possible to update the invoice"() {
        given:
        int id = service.save(invoices.get(0))

        when:
        service.update(id, invoices.get(1))

        then:
        service.getById(id).getBody() == invoices.get(1)
    }

}