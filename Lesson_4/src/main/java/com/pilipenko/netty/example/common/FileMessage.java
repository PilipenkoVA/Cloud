
package com.pilipenko.netty.example.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// Это посылка с данными файла
public class FileMessage extends AbstractMessage {
    private String filename;                                                            // имя файла
    private byte[] data;                                                                // байтовый массив

    public String getFilename() {
        return filename;
    }

    public byte[] getData() {
        return data;
    }

    public FileMessage(Path path) throws IOException {                                  // "FileMessage" создается из пути "Path"
        filename = path.getFileName().toString();
        data = Files.readAllBytes(path);
    }
}
