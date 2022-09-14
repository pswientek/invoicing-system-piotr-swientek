package pl.futurecollars.invoicing.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private UUID id;
    private String username;
    private String password;
    private String fullname;

}
