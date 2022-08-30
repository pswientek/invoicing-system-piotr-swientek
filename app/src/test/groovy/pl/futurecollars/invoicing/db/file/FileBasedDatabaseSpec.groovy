package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.db.AbstractDatabaseSpec
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.db.file.FileBasedDatabase
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.service.FileService
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.service.JsonService

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseSpec extends AbstractDatabaseSpec{

    @Override
    Database getDatabaseInstance() {

        String idFilePath = "./ids.txt"
        String dbPath = "./dbFile.txt"
        def idService = new IdService(new FileService(idFilePath))
        return new FileBasedDatabase<>(idService, new FileService(dbPath), new FileService(idFilePath), new JsonService(), Invoice)
    }

    def "file based database writes invoices to correct file"() {
        given:
        String dbPath = "./dbFile.txt"
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(4))

        then:
        1 == Files.readAllLines(Path.of(dbPath)).size()

        when:
        db.save(TestHelpers.invoice(5))

        then:
        2 == Files.readAllLines(Path.of(dbPath)).size()
    }
}
