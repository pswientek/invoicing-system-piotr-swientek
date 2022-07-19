package pl.futurecollars.invoicing.service

import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path

class FileServiceSpec extends Specification{

    private FileService fileService = new FileService()
    private Path path = File.createTempFile('lines', '.txt').toPath()

    def "line is correctly appended to file"() {
        setup:
        def testLine = "Test line to write"

        expect:
        [] == Files.readAllLines(path)

        when:
        fileService.appendLineToFile(path, testLine)

        then:
        [testLine] == Files.readAllLines(path)

        when:
        fileService.appendLineToFile(path, testLine)

        then:
        [testLine, testLine] == Files.readAllLines(path)
    }

    def "line is correctly written to file"() {
        expect:
        [] == Files.readAllLines(path)

        when:
        fileService.writeToFile(path, "1")

        then:
        ["1"] == Files.readAllLines(path)

        when:
        fileService.writeToFile(path, "2")

        then:
        ["2"] == Files.readAllLines(path)
    }

    def "list of lines is correctly written to file"() {
        given:
        def digits = ['1', '2', '3']
        def letters = ['a', 'b', 'c']

        expect:
        [] == Files.readAllLines(path)


        when:
        fileService.updateFile(path, digits)

        then:
        digits == Files.readAllLines(path)

        when:
        fileService.updateFile(path, letters)

        then:
        letters == Files.readAllLines(path)
    }

    def "line is correctly read from file"() {
        setup:
        def lines = List.of("line 1", "line 2", "line 3")
        Files.write(path, lines)

        expect:
        lines == fileService.readAllLines(path)
    }

    def "empty file returns empty collection"() {
        expect:
        [] == fileService.readAllLines(path)
    }
}
