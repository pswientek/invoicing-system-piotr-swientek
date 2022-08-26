package pl.futurecollars.invoicing.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.jpa.InvoiceRepository;
import pl.futurecollars.invoicing.db.jpa.JpaDatabase;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.db.sql.SqlDatabase;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Slf4j
@Configuration
public class DatabaseConfiguration {

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public IdService idService(
        @Value("${invoicing-system.database.id.file}") String idFile
    ) throws IOException {
        FileService fileService = new FileService(idFile);
        return new IdService(fileService);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public FileService fileService(
        @Value("${invoicing-system.database.invoices.file}") String invoicesFile
    ) throws IOException {
        return new FileService(invoicesFile);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    public Database fileBasedDatabase(
        @Value("${invoicing-system.database.invoices.file}") String invoicesFile,
        @Value("${invoicing-system.database.id.file}") String idFile
    ) throws IOException {
        IdService idService = new IdService(new FileService(idFile));
        return new FileBasedDatabase(idService, new FileService(invoicesFile), new FileService(idFile), new JsonService());
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    public Database inMemoryDatabase() {
        return new InMemoryDatabase();
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "sql")
    public Database sqlDatabase(JdbcTemplate jdbcTemplate) {
        return new SqlDatabase(jdbcTemplate);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "jpa")
    public Database jpaDatabase(InvoiceRepository invoiceRepository) {
        return new JpaDatabase(invoiceRepository);
    }

}
