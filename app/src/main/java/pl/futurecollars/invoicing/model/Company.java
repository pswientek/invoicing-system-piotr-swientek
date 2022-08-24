package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
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



}
