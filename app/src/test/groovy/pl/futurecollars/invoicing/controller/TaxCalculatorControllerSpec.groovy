package pl.futurecollars.invoicing.controller

import pl.futurecollars.invoicing.model.Car
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat
import spock.lang.Unroll

import java.time.LocalDate

import static pl.futurecollars.invoicing.TestHelpers.company

@Unroll
class TaxCalculatorControllerSpec extends AbstractControllerSpec {

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    def "zeros are returned when there are no invoices in the system"() {
        when:
        def taxCalculatorResponse = calculateTax(company(0))

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
        def taxCalculatorResponse = calculateTax(company(-1))

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
        def taxCalculatorResponse = calculateTax(company(5))

        then:
        taxCalculatorResponse.income == 30000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 30000
        taxCalculatorResponse.incomingVat == 2400.0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 2400.0

        when:
        taxCalculatorResponse = calculateTax(company(10))

        then:
        taxCalculatorResponse.income == 110000
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 110000
        taxCalculatorResponse.incomingVat == 8800.0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 8800.0

        when:
        taxCalculatorResponse = calculateTax(company(15))

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
        def taxCalculatorResponse = calculateTax(company(12))

        then:
        taxCalculatorResponse.income == 156000
        taxCalculatorResponse.costs == 6000
        taxCalculatorResponse.earnings == 150000
        taxCalculatorResponse.incomingVat == 12480.0
        taxCalculatorResponse.outgoingVat == 480.0
        taxCalculatorResponse.vatToReturn == 12000.0
    }

    def "tax is calculated correctly when car is not used for personal purposes"() {
        given:
        def invoice = Invoice.builder()
                .date(LocalDate.now())
                .number("9999")
                .seller(company(1))
                .buyer(company(2))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .vatValue(BigDecimal.valueOf(23.45))
                                .vatRate(Vat.VAT_8)
                                .price(BigDecimal.valueOf(100))
                                .quantity(1.0)
                                .carExpenses(
                                        Car.builder()
                                                .personalUsage(A)
                                                .registrationPlate("SGL 99999")
                                                .build()
                                )
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoice)

        when:
        def taxCalculatorResponse = calculateTax(invoice.getSeller())

        then: "no proportion - it applies only when you are the buyer"
        taxCalculatorResponse.income == income
        taxCalculatorResponse.costs == costs
        taxCalculatorResponse.earnings == earnings
        taxCalculatorResponse.incomingVat == iVat
        taxCalculatorResponse.outgoingVat == oVat
        taxCalculatorResponse.vatToReturn == vToReturn


        when:
        taxCalculatorResponse = calculateTax(invoice.getBuyer())

        then: "proportion applied - it applies when you are the buyer"
        taxCalculatorResponse.income == income2
        taxCalculatorResponse.costs == costs2
        taxCalculatorResponse.earnings == earnings2
        taxCalculatorResponse.incomingVat == iVat2
        taxCalculatorResponse.outgoingVat == oVat2
        taxCalculatorResponse.vatToReturn == vToReturn2

        where:
        A       ||   income  |   costs   |   earnings    |   iVat    |   oVat    |   vToReturn |   income2 |   costs2  |   earnings2   |   iVat2   |   oVat2   |   vToReturn2
        true    ||   100     |   0       |   100         |   23.45   |   0       |   23.45     |   0       |   111.73  |   -111.73     |   0       |   23.45   |   -23.45
        false   ||   100     |   0       |   100         |   23.45   |   0       |   23.45     |   0       |   100     |   -100        |   0       |   23.45   |   -23.45


    }

    def "All calculations are executed correctly"() {
        given:
        def ourCompany = Company.builder()
                .taxIdentificationNumber("1234")
                .address("no address exception ;)")
                .name("i don't care about name")
                .pensionInsurance(514.57)
                .healthInsurance(319.94)
                .build()

        def invoiceWithIncome = Invoice.builder()
                .date(LocalDate.now())
                .number("number is required")
                .seller(ourCompany)
                .buyer(company(2))
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(76011.62)
                                .vatValue(0.0)
                                .quantity(1.0)
                                .vatRate(Vat.VAT_0)
                                .build()
                ))
                .build()

        def invoiceWithCosts = Invoice.builder()
                .date(LocalDate.now())
                .number("number is required")
                .seller(company(4))
                .buyer(ourCompany)
                .entries(List.of(
                        InvoiceEntry.builder()
                                .price(11329.47)
                                .vatValue(0.0)
                                .quantity(1.0)
                                .vatRate(Vat.VAT_0)
                                .build()
                ))
                .build()

        addInvoiceAndReturnId(invoiceWithIncome)
        addInvoiceAndReturnId(invoiceWithCosts)

        when:
        def taxCalculatorResponse = calculateTax(ourCompany)

        then:
        with(taxCalculatorResponse) {
            income == 76011.62
            costs == 11329.47
            earnings == 64682.15
            pensionInsurance == 514.57
            earningsSubPensionInsurance == 64167.58
            earningsSubPensionInsuranceRounded == 64168
            incomeTax == 12191.92
            healthInsuranceReference == 319.94
            healthInsuranceReduce == 275.50
            incomeTaxMinusHealthInsurance == 11916.42
            finalIncomeTax == 11916

            incomingVat == 0
            outgoingVat == 0
            vatToReturn == 0
        }
    }
}
