package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.JsonService
import pl.futurecollars.invoicing.service.TaxCalculatorResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice

@AutoConfigureMockMvc
@SpringBootTest
@Unroll
class TaxCalculatorControllerSpec extends Specification{

    static final String INVOICE_ENDPOINT = "/invoices"
    static final String TAX_CALCULATOR_ENDPOINT = "/tax"

    @Autowired
    MockMvc mockMvc

    @Autowired
    JsonService jsonService

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    def "zeros are returned when there are no invoices in the system"() {
        when:
        def taxCalculatorResponse = calculateTax("0")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "zeros are returned when tax id is not matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax("no_match")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "sum of all products is returned when tax id is matching"() {
        given:
        addUniqueInvoices(10)

        when:
        def taxCalculatorResponse = calculateTax("5")

        then:
        taxCalculatorResponse.income == 30000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 30000
        taxCalculatorResponse.incomingVat == 2400.0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 2400.0

        when:
        taxCalculatorResponse = calculateTax("10")

        then:
        taxCalculatorResponse.income == 110000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 110000
        taxCalculatorResponse.incomingVat == 8800.0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 8800.0

        when:
        taxCalculatorResponse = calculateTax("15")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 30000
        taxCalculatorResponse.earnings == -30000
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 2400.0
        taxCalculatorResponse.vatToReturn == -2400.0
    }

    def "correct values are returned when company was buyer and seller"() {
        given:
        addUniqueInvoices(15) // sellers: 1-15, buyers: 10-25, 10-15 overlapping

        when:
        def taxCalculatorResponse = calculateTax("12")

        then:
        taxCalculatorResponse.income == 156000
        taxCalculatorResponse.costs == 6000
        taxCalculatorResponse.earnings == 150000
        taxCalculatorResponse.incomingVat == 12480.0
        taxCalculatorResponse.outgoingVat == 480.0
        taxCalculatorResponse.vatToReturn == 12000.0
    }

    TaxCalculatorResult calculateTax(String taxIdentificationNumber) {
        def response = mockMvc.perform(get("$TAX_CALCULATOR_ENDPOINT/$taxIdentificationNumber"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.returnJsonAsInvoice(response, TaxCalculatorResult)
    }

    List<Invoice> addUniqueInvoices(int count) {
        (1..count).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(jsonService.invoiceAsJson(invoice))
            return invoice
        }
    }

    int addInvoiceAndReturnId(String invoiceAsJson) {
        Integer.valueOf(
                mockMvc.perform(
                        post(INVOICE_ENDPOINT)
                                .content(invoiceAsJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isOk())
                        .andReturn()
                        .response
                        .contentAsString
        )
    }

    List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get(INVOICE_ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.returnJsonAsInvoice(response, Invoice[])
    }

    void deleteInvoice(int id) {
        mockMvc.perform(delete("$INVOICE_ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

}
