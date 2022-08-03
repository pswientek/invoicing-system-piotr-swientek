package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.service.FileService
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.service.JsonService

import java.nio.file.Files
import java.nio.file.Path

class FileBasedDatabaseSpec extends AbstractDatabaseSpec{

    def dbPath

    @Override
    Database getDatabaseInstance() {

        String idFilePath = "./ids.txt"
        String dbPath = "./dbFile.txt"
        def idFileService = new FileService(idFilePath)
        def idService = new IdService(idFileService)
        def dbFileService = new FileService(dbPath)
        return new FileBasedDatabase(idService, dbFileService, new JsonService())
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()
        //String dbPath = "./dbFile.txt"

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
