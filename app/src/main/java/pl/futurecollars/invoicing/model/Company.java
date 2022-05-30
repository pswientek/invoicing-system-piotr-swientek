package pl.futurecollars.invoicing.model;

public class Company {

    private final String id;
    private final String taxIdentificationNumber;
    private final String address;

    public Company(String id, String taxIdentificationNumber, String address) {
        this.id = id;
        this.taxIdentificationNumber = taxIdentificationNumber;
        this.address = address;
    }
}
