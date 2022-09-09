package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
public class FileDatabaseConfiguration {

    @Bean
    public IdService idService(
        @Value("${invoicing-system.database.id.file}") String idFile
    ) throws IOException {
        FileService fileService = new FileService(idFile);
        return new IdService(fileService);
    }

    @Bean
    public FileService fileService(
        @Value("${invoicing-system.database.invoices.file}") String invoicesFile
    ) throws IOException {
        return new FileService(invoicesFile);
    }

    @Bean
    public Database<Invoice> invoiceFileBasedDatabase(
        @Value("${invoicing-system.database.invoices.file}") String invoicesFile,
        @Value("${invoicing-system.database.id.invoices.file}") String idFile
    ) throws IOException {
        IdService idService = new IdService(new FileService(idFile));
        return new FileBasedDatabase<>(idService, new FileService(invoicesFile), new FileService(idFile), new JsonService(), Invoice.class);
    }

    @Bean
    public Database<Company> companyFileBasedDatabase(
        @Value("${invoicing-system.database.id.companies.file}") String idFile,
        @Value("${invoicing-system.database.companies.file}") String companiesFile
    ) throws IOException {
        IdService idService = new IdService(new FileService(idFile));
        return new FileBasedDatabase<>(idService, new FileService(companiesFile), new FileService(idFile), new JsonService(), Company.class);
    }

}
