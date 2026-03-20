package tool.stock.v8sniper.service;

import org.springframework.stereotype.Service;
import tool.stock.v8sniper.config.V8Properties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class FileStorageService {
    private final V8Properties properties;
    public FileStorageService(V8Properties properties) { this.properties = properties; }
    public Path rawFile(LocalDate date, String fileName) throws IOException {
        Path dir = Path.of(properties.getDataRoot(), "raw", date.format(DateTimeFormatter.BASIC_ISO_DATE));
        Files.createDirectories(dir);
        return dir.resolve(fileName);
    }
    public Path exportFile(LocalDate date, String fileName) throws IOException {
        Path dir = Path.of(properties.getDataRoot(), "export", date.format(DateTimeFormatter.BASIC_ISO_DATE));
        Files.createDirectories(dir);
        return dir.resolve(fileName);
    }
}
