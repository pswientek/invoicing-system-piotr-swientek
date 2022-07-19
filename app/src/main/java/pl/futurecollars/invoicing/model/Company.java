package pl.futurecollars.invoicing.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Company {

    private String id;
    private String taxIdentificationNumber;
    private String address;

    public Company(String id, String taxIdentificationNumber, String address) {
        this.id = id;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }
}
