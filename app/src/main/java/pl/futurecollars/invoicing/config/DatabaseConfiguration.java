package pl.futurecollars.invoicing.config;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
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
import pl.futurecollars.invoicing.db.mongo.MongoBasedDatabase;
import pl.futurecollars.invoicing.db.sql.SqlDatabase;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;
import pl.futurecollars.invoicing.service.MongoIdService;

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

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public MongoDatabase mongoDb(
        @Value("${invoicing-system.database.name}") String databaseName
    ) {
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
            fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodecRegistry)
            .build();

        MongoClient client = MongoClients.create(settings);
        return client.getDatabase(databaseName);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public MongoIdService mongoIdService(
        @Value("${invoicing-system.database.counter.collection}") String collectionName,
        MongoDatabase mongoDb
    ) {
        MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
        return new MongoIdService(collection);
    }

    @Bean
    @ConditionalOnProperty(name = "invoicing-system.database", havingValue = "mongo")
    public Database mongoDatabase(
        @Value("${invoicing-system.database.collection}") String collectionName,
        MongoDatabase mongoDb,
        MongoIdService mongoIdService
    ) {
        MongoCollection<Invoice> collection = mongoDb.getCollection(collectionName, Invoice.class);
        return new MongoBasedDatabase(collection, mongoIdService);
    }

}
