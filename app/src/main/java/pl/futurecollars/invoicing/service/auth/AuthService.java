package pl.futurecollars.invoicing.service.auth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.common.JwtUtil;
import pl.futurecollars.invoicing.controller.auth.LoginRequestDto;
import pl.futurecollars.invoicing.controller.auth.LoginResponseDto;
import pl.futurecollars.invoicing.model.User;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;

    public List<User> users = new ArrayList<>() {
        {
            add(new User(UUID.randomUUID(), "piotr", "nieWiemCzyZdaze", "Gumka Olowek"));
        }
    };

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        User user = users.stream().filter(u -> u.getUsername().equals(loginRequestDto.getUsername())).findAny()
          .orElseThrow(NoSuchElementException::new);

        if (user.getPassword().equals(loginRequestDto.getPassword())) {
            String token = jwtUtil.doGenerateToken(new HashMap<>(), user.getUsername());
            return LoginResponseDto.builder().token(token).build();
        }
        throw new IllegalArgumentException();
    }

    public void logout() {

    }

}
