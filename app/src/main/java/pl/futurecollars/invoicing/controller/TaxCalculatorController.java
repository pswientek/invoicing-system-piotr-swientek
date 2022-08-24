package pl.futurecollars.invoicing.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.TaxCalculatorResult;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@RestController
@AllArgsConstructor
public class TaxCalculatorController implements TaxCalculatorApi {

    private final TaxCalculatorService taxCalculatorService;

    @Override
    public TaxCalculatorResult calculateTaxes(@RequestBody Company company) {
        return taxCalculatorService.calculateTaxes(company);
    }

}
