package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

class JsonServiceSpec extends Specification {

    def "can convert object to json and read it back"() {
        given:
        def jsonService = new JsonService()
        def invoice = TestHelpers.invoice(12)

        when:
        def invoiceAsString = jsonService.objectAsJson(invoice)
        System.out.println(invoiceAsString)
        and:
        def invoiceFromJson = jsonService.returnJsonAsInvoice(invoiceAsString, Invoice.class)

        then:
        invoice == invoiceFromJson
    }

}