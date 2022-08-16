package pl.futurecollars.invoicing.config;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.memory.FileBasedDatabase;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Slf4j
@Configuration
public class DatabaseConfiguration {

    @Value("${invoicing-system.database.id.file}")
    public String idFile;
    @Value("${invoicing-system.database.invoices.file}")
    public String invoicesFile;

    @Bean
    public IdService idService() throws IOException {
        FileService fileService = new FileService(idFile);
        return new IdService(fileService);
    }

    @Bean
    public FileService fileService() throws IOException {
        return new FileService(invoicesFile);
    }

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "file")
    @Bean
    public Database fileBasedDatabase() throws IOException {
        log.debug("File based database was created");
        IdService idService = new IdService(new FileService(idFile));
        return new FileBasedDatabase(idService, new FileService(invoicesFile), new FileService(idFile), new JsonService());
    }

    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "memory")
    @Bean
    public Database inMemoryDatabase() {
        log.debug("In memory database was created");
        return new InMemoryDatabase();
    }

}
