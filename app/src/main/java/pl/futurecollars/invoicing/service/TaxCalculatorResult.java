package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class TaxCalculatorResult {

    private BigDecimal income;
    private BigDecimal costs;
    private BigDecimal earnings;
    private BigDecimal pensionInsurance;
    private BigDecimal earningsSubPensionInsurance;
    private BigDecimal earningsSubPensionInsuranceRounded;
    private BigDecimal incomeTax;
    private BigDecimal healthInsuranceReference;
    private BigDecimal healthInsuranceReduce;
    private BigDecimal incomeTaxMinusHealthInsurance;
    private BigDecimal finalIncomeTax;

    private BigDecimal incomingVat;
    private BigDecimal outgoingVat;
    private BigDecimal vatToReturn;

}
