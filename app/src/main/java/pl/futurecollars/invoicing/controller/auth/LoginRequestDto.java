package pl.futurecollars.invoicing.controller.auth;

import lombok.Value;

@Value
public class LoginRequestDto {

    String username;
    String password;

}
