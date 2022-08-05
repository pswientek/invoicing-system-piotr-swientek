package pl.futurecollars.invoicing.config;

import java.io.IOException;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.memory.FileBasedDatabase;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@Configuration
public class DatabaseConfiguration {

    private static final String ID_FILE_NAME = "ids.txt";
    private static final String INVOICES_FILE_NAME = "dbFile.txt";

    @Bean
    public IdService idService() throws IOException {
        FileService fileService = new FileService(ID_FILE_NAME);
        return new IdService(fileService);
    }

    @SneakyThrows
    @Bean
    public Database fileBasedDatabase() {
        IdService idService = new IdService(new FileService(ID_FILE_NAME));
        return new FileBasedDatabase(idService, new FileService(INVOICES_FILE_NAME), new FileService(ID_FILE_NAME), new JsonService());
    }

}
