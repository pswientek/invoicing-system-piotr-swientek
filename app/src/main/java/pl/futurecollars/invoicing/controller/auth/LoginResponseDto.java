package pl.futurecollars.invoicing.controller.auth;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LoginResponseDto {

    String token;
}
