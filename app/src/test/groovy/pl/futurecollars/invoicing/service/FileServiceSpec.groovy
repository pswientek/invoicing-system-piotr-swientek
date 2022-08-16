package pl.futurecollars.invoicing.service

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FileServiceSpec extends Specification{

    private String path = "./fileServiceDb.txt"
    private FileService fileService = new FileService(path)

    def "creating file after fileService initialization"() {
        expect:
        Files.exists(Path.of(path))
    }

    def "line is correctly appended to file"() {
        setup:
        def testLine = "Test line to write"

        expect:
        [] == Files.readAllLines(Path.of(path))

        when:
        fileService.appendLineToFile(testLine)

        then:
        [testLine] == Files.readAllLines(Path.of(path))

        when:
        fileService.appendLineToFile(testLine)

        then:
        [testLine, testLine] == Files.readAllLines(Path.of(path))
    }

    def "line is correctly written to file"() {
        expect:
        [] == Files.readAllLines(Path.of(path))

        when:
        fileService.writeToFile("1")

        then:
        ["1"] == Files.readAllLines(Path.of(path))

        when:
        fileService.writeToFile("2")

        then:
        ["2"] == Files.readAllLines(Path.of(path))
    }

    def "list of lines is correctly written to file"() {
        given:
        def digits = ['1', '2', '3']
        def letters = ['a', 'b', 'c']

        expect:
        [] == Files.readAllLines(Path.of(path))


        when:
        fileService.updateFile(digits)

        then:
        digits == Files.readAllLines(Path.of(path))

        when:
        fileService.updateFile(letters)

        then:
        letters == Files.readAllLines(Path.of(path))
    }

    def "line is correctly read from file"() {
        setup:
        def lines = List.of("line 1", "line 2", "line 3")
        Files.write(Path.of(path), lines)

        expect:
        lines == fileService.readAllLines()
    }

    def "empty file returns empty collection"() {
        expect:
        [] == fileService.readAllLines()
    }
}
