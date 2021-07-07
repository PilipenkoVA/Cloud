package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Класс содержит данные о файлах
public class FileInfo {
    public enum FileType{
        FILE("F"), DIRECTORY("D");

        private String name;                                // тип файла "F" или "D" для "enum"

        public String getName() {
            return name;
        }

        FileType(String name) {
            this.name = name;
        }
    }
    private String filename;                                // имя файла
    private FileType type;                                  // тип файла
    private long size;                                      // размер
    private LocalDateTime lastModified;                     // дата последнего изменения

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
    // По указанному пути сможем получить "client.FileInfo"
    public FileInfo(Path path){
        try {
            this.filename = path.getFileName().toString();          // имя файла
            this.size = Files.size(path);                           // размер файла в байтах
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;// тип файла (если файл не явл. "DIRECTORY", то это файл)
            if (this.type == FileType.DIRECTORY){                   // если файл явл. "DIRECTORY"
                this.size = -1L;                                    // его размер будет -1 (необходимо для дальнейшей сортировки)
            }
            // дату последнего изменения
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneOffset.ofHours(3));// с указанием часового пояса (+3 часа)
        } catch (IOException e) {
            throw new RuntimeException("Unable to create file from path");
        }

    }
}
