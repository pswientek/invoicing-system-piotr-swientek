package pl.futurecollars.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
public class InvoicingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoicingSystemApplication.class, args);
    }
}
