package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Company {

    @ApiModelProperty(value = "Tax identification number", required = true, example = "999-666-22-00")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address", required = true, example = "ul. Wodna 15, 00-120 Nibylandia")
    private String address;

    @ApiModelProperty(value = "Company name", required = true, example = "Invoice Charger Ltd.")
    private String name;

    @Builder.Default
    @ApiModelProperty(value = "Pension insurance amount", required = true, example = "1328.75")
    private BigDecimal pensionInsurance = BigDecimal.ZERO;

    @Builder.Default
    @ApiModelProperty(value = "Health insurance amount", required = true, example = "458.34")
    private BigDecimal healthInsurance = BigDecimal.ZERO;



}
