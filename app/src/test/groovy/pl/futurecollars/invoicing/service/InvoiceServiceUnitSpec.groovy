package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import spock.lang.Specification
import static pl.futurecollars.invoicing.TestHelpers.invoice

class InvoiceServiceUnitSpec extends Specification {

    private InvoiceService service
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

    def "calling getAll() should delegate to database getAll() method"() {
        when:
        service.getAll()
        then:
        1 * database.getAll()
    }
}