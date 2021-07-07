package com.pilipenko.netty.example.common;

public class FileRequest extends AbstractMessage {                      // наследуемся от "AbstractMessage"
    private String filename;                                           // и через имя файла понимает что клиент от нас хочет

    public String getFilename() {
        return filename;
    }

    public FileRequest(String filename) {
        this.filename = filename;
    }
}