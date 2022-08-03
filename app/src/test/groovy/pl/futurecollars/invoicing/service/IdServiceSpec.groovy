package pl.futurecollars.invoicing.service

import spock.lang.Specification
import java.nio.file.Files
import java.nio.file.Path

class IdServiceSpec extends Specification {

    def "next id starts from 1 if file was empty"() {
        given:
        String path = "./ids.txt"
        FileService fileService = new FileService(path)
        IdService idService = new IdService(fileService)

        expect:
        ['1'] == Files.readAllLines(Path.of(path))

        and:
        1 == idService.getNextIdAndIncrement(fileService)
        ['2'] == Files.readAllLines(Path.of(path))

        and:
        2 == idService.getNextIdAndIncrement(fileService)
        ['3'] == Files.readAllLines(Path.of(path))

        and:
        3 == idService.getNextIdAndIncrement(fileService)
        ['4'] == Files.readAllLines(Path.of(path))
    }

    def "next id starts from last number if file was not empty"() {
        given:
        String path = "./ids.txt"
        FileService fileService = new FileService(path)
        Files.writeString(Path.of(path), "17")
        IdService idService = new IdService(fileService)

        expect:
        ['17'] == Files.readAllLines(Path.of(path))

        and:
        17 == idService.getNextIdAndIncrement(fileService)
        ['18'] == Files.readAllLines(Path.of(path))

        and:
        18 == idService.getNextIdAndIncrement(fileService)
        ['19'] == Files.readAllLines(Path.of(path))

        and:
        19 == idService.getNextIdAndIncrement(fileService)
        ['20'] == Files.readAllLines(Path.of(path))
    }
}