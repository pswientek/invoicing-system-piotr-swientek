package pl.futurecollars.invoicing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@SpringBootApplication
@ComponentScan
public class InvoicingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(InvoicingSystemApplication.class, args);
    }
}
