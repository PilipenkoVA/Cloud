package com.pilipenko.netty.example.client;

import com.pilipenko.netty.example.common.AbstractMessage;
import com.pilipenko.netty.example.common.FileMessage;
import com.pilipenko.netty.example.common.FileRequest;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    TextField tfFileName;                                                            // имя файла который нам необходим

    @FXML
    ListView<String> filesList;                                                      // таблица файлов которые есть на клиенте

    // Запуск потока
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Network.start();                                                            // при запуске клиента подключаемся к серверу
        System.out.println("Client started...");
        // Далее создается "Thread"
        Thread t = new Thread(() -> {
            try {
                while (true) {                                                      // который в бесконечном потоке ждет сообщение от сервера
                    AbstractMessage am = Network.readObject();                      // блокирующая операция (постоянно находимся в ожидании)

                    if (am instanceof FileMessage) {                                // если сервер прислал "FileMessage"
                        FileMessage fm = (FileMessage) am;                          // делаем каст

                        // вытягиваем из "FileMessage" все байты и помещаем в "client_storage"
                        Files.write(Paths.get("client_storage/" + fm.getFilename()), fm.getData(), StandardOpenOption.CREATE);
                        refreshLocalFilesList();                                    // обновляем список файлов
                        System.out.println("Download file: " + fm.getFilename());
                    }
                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();

            }
        });
        t.setDaemon(true);
        t.start();
        refreshLocalFilesList();
    }
    // Нажатие на кнопку "Скачать"
    public void pressOnDownloadBtn(ActionEvent actionEvent) {                        // когда нажимаем на кнопку скачать
        if (tfFileName.getLength() > 0) {
            Network.sendMsg(new FileRequest(tfFileName.getText()));                  // посылаем в сторону сервера "FileRequest" с именем интересующего нас файла
            tfFileName.clear();                                                      // очищаем поле для ввода
        }
    }
//     Нажатие на кнопку "Загрузить"
    public void pressOnUploadBtn(ActionEvent actionEvent) {                        // когда нажимаем на кнопку загрузить
        System.out.println("Загрузить файл: " + tfFileName.getText());
        tfFileName.clear();                                                        // очищаем поле для ввода
    }

    // Обновление списка локалиных файлов "FilesList"
    public void refreshLocalFilesList() {
        Platform.runLater(() -> {
            try {
                filesList.getItems().clear();                                       // очищаем "filesList"

                // записываем все файлы которые есть в "client_storage"
                Files.list(Paths.get("client_storage")).map(p -> p.getFileName().toString()).forEach(o -> filesList.getItems().add(o));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    // Выход из программы
    public void btnExit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
