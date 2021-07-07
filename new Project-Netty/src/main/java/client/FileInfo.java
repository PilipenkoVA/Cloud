package client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {

    public static final String UP_TOKEN = "[...]";

    private String fileName;
    private long length;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public boolean isDirectory(){
        return length == -1L;            // если размер файла равен "-1" , то значит это директория
    }

    public boolean isUpElement() {       // ВВЕРХ: проверка для перехода на уровень вверх
        return length == -2L;
    }
    // КОНСТРУКТОР: ВВЕРХ: необходим для перехода на уровень вверх
    public FileInfo(String fileName, long length){
        this.fileName = fileName;
        this.length = length;
    }

    public FileInfo(Path path){
        try{
            this.fileName = path.getFileName().toString();
            if (Files.isDirectory(path)){
                this.length = -1L;
            }else {
                this.length = Files.size(path);
            }
        }catch (IOException e) {
            throw new RuntimeException("Unable to create file from path"+path.toAbsolutePath().toString());
        }
    }

}
