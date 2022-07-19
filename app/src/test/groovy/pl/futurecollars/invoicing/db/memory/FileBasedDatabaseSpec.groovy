package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.TestHelpers
import pl.futurecollars.invoicing.service.FileService
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.service.JsonService

import java.nio.file.Files

class FileBasedDatabaseSpec extends AbstractDatabaseSpec{

    def dbPath

    @Override
    Database getDatabaseInstance() {
        def fileService = new FileService()

        def idFile =  File.createTempFile("ids", ".txt")
        def idPath = idFile.toPath()
        def idService = new IdService(idPath, fileService)

        def dbFile = File.createTempFile("dbData", ".txt")
        dbPath = dbFile.toPath()
        return new FileBasedDatabase(dbPath, idService, fileService, new JsonService())
    }

    def "file based database writes invoices to correct file"() {
        given:
        def db = getDatabaseInstance()

        when:
        db.save(TestHelpers.invoice(4))

        then:
        1 == Files.readAllLines(dbPath).size()

        when:
        db.save(TestHelpers.invoice(5))

        then:
        2 == Files.readAllLines(dbPath).size()
    }
}
