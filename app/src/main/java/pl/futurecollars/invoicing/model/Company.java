package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Company {

    private String id;

    @ApiModelProperty(value = "Tax identification number", required = true, example = "999-666-22-00")
    private String taxIdentificationNumber;

    @ApiModelProperty(value = "Company address and name", required = true, example = "ul. Wodna 15, 00-120 Nibylandia, Invoice Charger Ltd.")
    private String address;

    public Company(String id, String taxIdentificationNumber, String address) {
        this.id = id;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }
}
