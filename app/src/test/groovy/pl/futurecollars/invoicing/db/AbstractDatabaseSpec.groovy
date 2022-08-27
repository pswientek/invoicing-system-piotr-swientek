package pl.futurecollars.invoicing.db

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification
import static pl.futurecollars.invoicing.TestHelpers.invoice

abstract class AbstractDatabaseSpec extends Specification {

    List<Invoice> invoices = (1..12).collect { invoice(it) }

    abstract Database getDatabaseInstance()

    Database database

    def setup() {
        database = getDatabaseInstance()
        database.reset()
    }

    def "should save invoices returning sequential id"() {
        when:
        def ids = invoices.collect {it.id = database.save(it) }

        then:
        (1..invoices.size() - 1).forEach {assert ids[it] == ids[0] + it }
    }

    def "invoice should have id set to correct value"() {
        when:
        def ids = invoices.collect {it.id = database.save(it) }

        then:
        ids.forEach {assert database.getById(it).isPresent() }
        ids.forEach {assert database.getById(it).get().getId() == it }
    }

    def "get by id returns expected invoice"() {
        when:
        def ids = invoices.collect { it.id = database.save(it) }

        then:
        ids.forEach {
            def expectedInvoice = resetIds(invoices.get((int) (it - ids[0]))).toString()
            def invoiceFromDb = resetIds(database.getById(it).get()).toString()

            assert invoiceFromDb == expectedInvoice
        }
    }

    def "get by id returns empty optional when there is no invoice with given id"() {
        expect:
        !database.getById(1).isPresent()
    }

    def "get all returns empty collection if there were no invoices"() {
        expect:
        database.getAll().isEmpty()
    }

    def "get all returns all invoices in the database, deleted invoice is not returned"() {
        given:
        invoices.forEach { it.id = database.save(it) }

        expect:
        database.getAll().size() == invoices.size()
        database.getAll().forEach { assert resetIds(it) == invoices.get(it.getId() - 1 as int) }

        when:
        database.delete(1)

        then:
        database.getAll().size() == invoices.size() - 1
        database.getAll().forEach { assert resetIds(it) == invoices.get(it.getId() - 1 as int) }
        database.getAll().forEach { assert it.getId() != 1 }
    }

    def "it's possible to update the invoice, original invoice is returned"() {
        given:
        def originalInvoice = invoices.get(0)
        originalInvoice.id = database.save(originalInvoice)

        def expectedInvoice = invoices.get(1)
        expectedInvoice.id = originalInvoice.id

        when:
        def result = database.update(originalInvoice.id, expectedInvoice)

        then:
        def invoiceAfterUpdate = database.getById(originalInvoice.id).get()
        resetIds(invoiceAfterUpdate) == expectedInvoice
        resetIds(result.get()) == originalInvoice
    }


    def "can delete all invoices"() {
        given:
        invoices.forEach { it.id = database.save(it) }

        when:
        invoices.forEach { database.delete(it.getId()) }

        then:
        database.getAll().isEmpty()
    }

    def "deleting not existing invoice is not causing any error"() {
        expect:
        database.delete(123) == Optional.empty()
    }

    def "updating not existing invoice throws exception"() {
        expect:
        database.update(213, invoices.get(1)) == Optional.empty()
    }

    static Invoice resetIds(Invoice invoice) {
        invoice.getBuyer().id = 0
        invoice.getSeller().id = 0
        invoice
    }


}