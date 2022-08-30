package pl.futurecollars.invoicing.controller.tax;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.tax.TaxCalculatorResult;
import pl.futurecollars.invoicing.service.tax.TaxCalculatorService;

@RestController
@AllArgsConstructor
public class TaxCalculatorController implements TaxCalculatorApi {

    @Autowired
    private final TaxCalculatorService taxCalculatorService;

    @Override
    public TaxCalculatorResult calculateTaxes(@RequestBody Company company) {
        return taxCalculatorService.calculateTaxes(company);
    }

}
