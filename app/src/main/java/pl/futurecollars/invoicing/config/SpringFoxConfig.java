package pl.futurecollars.invoicing.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .tags(
                        new Tag("invoice-controller", "Controller used to list / add / update / delete invoices."),
                        new Tag("tax-controller", "Controller that is calculating taxes")
                )
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .description("Invoice application")
                .license("No licence available - private!")
                .title("Private Invoicing")
                .contact(
                        new Contact(
                                "Piotr Swientek",
                                "https://github.com/pswientek/",
                                "swientek-dev@gmail.com")
                )
                .build();
    }
}
