package pl.futurecollars.invoicing.model;

import javax.persistence.Id;

public interface IdInterface {

    @Id
    long getId();

    @Id
    void setId(long id);
}
