package pl.futurecollars.invoicing.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileService {

    private final String filePathName;

    public FileService(String filePathName) throws IOException {
        this.filePathName = filePathName;
        if (Files.exists(Path.of(filePathName))) {
            Files.delete(Path.of(filePathName));
        }
        Files.createFile(Path.of(filePathName));
    }

    public void appendLineToFile(String line) throws IOException {
        Files.write(Path.of(filePathName), (line + System.lineSeparator()).getBytes(), StandardOpenOption.APPEND);
    }

    public void writeToFile(String line) throws IOException {
        Files.write(Path.of(filePathName), line.getBytes(), StandardOpenOption.TRUNCATE_EXISTING);
    }

    public List<String> readAllLines() throws IOException {
        return Files.readAllLines(Path.of(filePathName));
    }

    public void updateFile(List<String> lines) throws IOException {
        Files.write(Path.of(filePathName), lines, StandardOpenOption.TRUNCATE_EXISTING);
    }

}
