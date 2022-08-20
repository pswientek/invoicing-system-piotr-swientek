package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.service.TaxCalculatorResult;

@RequestMapping("tax")
@Api(tags = {"tax-controller"})
public interface TaxCalculatorApi {

    @ApiOperation(value = "Get the value of taxes")
    @GetMapping(value = "/{taxIdentificationNumber}", produces = {"application/json;charset=UTF-8"})
    TaxCalculatorResult calculateTaxes(@PathVariable @ApiParam(example = "999-666-00-12") String taxIdentificationNumber);

}
