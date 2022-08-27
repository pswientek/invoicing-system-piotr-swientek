package pl.futurecollars.invoicing.service

import org.springframework.beans.factory.annotation.Autowired
import pl.futurecollars.invoicing.db.Database
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.invoice

class InvoiceServiceUnitSpec extends Specification {

    @Autowired
    private InvoiceService service

    @Autowired
    private Database database

    def setup() {
        database = Mock()
        service = new InvoiceService(database)
    }

    def "calling save() should delegate to database save() method"() {
        given:
        def invoice = invoice(1)
        when:
        service.save(invoice)
        then:
        1 * database.save(invoice)
    }
    def "calling delete() should delegate to database delete() method"() {
        given:
        def invoiceId = 15
        when:
        service.delete(invoiceId)
        then:
        1 * database.delete(invoiceId)
    }
    def "calling getById() should delegate to database getById() method"() {
        given:
        def invoiceId = 99
        when:
        service.getById(invoiceId)
        then:
        1 * database.getById(invoiceId)
    }
    def "calling getAll() should delegate to database getAll() method"() {
        when:
        service.getAll()
        then:
        1 * database.getAll()
    }
    def "calling update() should delegate to database update() method"() {
        given:
        def invoice = invoice(1)
        invoice.id = 1
        when:
        service.update(invoice.getId(), invoice)
        then:
        1 * database.update(invoice.getId(), invoice)
    }
}